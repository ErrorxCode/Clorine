package com.errorxcode.clorine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A cache that can store objects and strings. The cache is stored on the disk
 * and is loaded into the memory when the cache is created. It is thread
 * safe and can be accessed from multiple threads at the same time. It
 * is also asynchronous and does not block the calling thread. The cache is
 * stored in a folder with the name of the cache and is stored in the disk
 * in the following format:
 * <p>
 *     <code>
 *         cacheFolder/ <br>
 *         &nbsp;&nbsp;&nbsp;&nbsp;primitive.cache <br>
 *         &nbsp;&nbsp;&nbsp;&nbsp;key1.obj <br>
 *         &nbsp;&nbsp;&nbsp;&nbsp;key2.obj <br>
 *         &nbsp;&nbsp;&nbsp;&nbsp;... <br>
 *         &nbsp;&nbsp;&nbsp;&nbsp;keyN.obj <br>
 *         &nbsp;&nbsp;&nbsp;&nbsp;keyN+1.obj <br>
 *         &nbsp;&nbsp;&nbsp;&nbsp;... <br>
 */
public class Cache {
    private final File folder;
    private final File primitiveFile;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final LinkedHashMap<String, Serializable> map;

    protected Cache(int size, File folder) {
        this.map = new LinkedHashMap<>(size + 1) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Serializable> eldest) {
                return size() > size;
            }
        };
        this.folder = folder;
        primitiveFile = new File(folder, "primitive.cache");
    }

    /**
     * Put an object in the cache
     * @param key the key of the object
     * @param object the object
     * @param <T> the type of the object
     * @throws RuntimeException if the object cannot be saved on the disk due to IO error
     */
    public synchronized <T> void put(String key,Cacheable object) {
        if (key == null || object == null)
            throw new IllegalArgumentException("key and object can't be null");

        map.put(key, object);
        executor.submit(() -> {
            try {
                var os = new ObjectOutputStream(new FileOutputStream(new File(folder, key + ".obj")));
                os.writeObject(object);
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Put a string in the cache.
     * @param key the key of the string
     * @param value the string
     */
    public synchronized void put(String key,String value){
        map.put(key,value);
        executor.submit(() -> FileUtils.appendLine(primitiveFile, key + "=" + value));
    }

    /**
     * Remove an object or string from the cache
     * @param key the key of the object or string
     */
    public synchronized void remove(String key) {
        map.remove(key);
        executor.submit(() -> {
            var file = new File(folder, key + ".obj");
            if (file.exists())
                file.delete();
            else
                FileUtils.removeLine(primitiveFile, key);
        });
    }

    /**
     * Clear the cache in the memory. Does not clear the cache on the disk
     */
    public void clear() {
        map.clear();
    }

    /**
     * Get an object from the cache. The object can either be a string or a POJO
     * @param key the key of the cached data
     * @param clazz the class of the object
     * @param <T> the type of the object
     * @return the object
     * @throws RuntimeException if the object cannot be read from the disk due to IO error
     */
    public synchronized <T> T get(String key, Class<T> clazz) {
        var value = map.get(key);
        if (value == null) {
            if (clazz == String.class) {
                var prop = FileUtils.readLine(primitiveFile, key);
                if (prop == null)
                    return null;
                else
                    value = prop.split("=")[1];

                map.put(key,value);
            } else if (clazz.isPrimitive() || clazz == Integer.class || clazz == Double.class || clazz == Float.class || clazz == Long.class || clazz == Boolean.class || clazz == Character.class) {
                throw new IllegalArgumentException("Use String.class instead of " + clazz.getName() + " & then parse it to your desired type");
            } else {
                try {
                    var in = new ObjectInputStream(new FileInputStream(new File(folder, key + ".obj")));
                    value = (Serializable) in.readObject();
                    in.close();
                    map.put(key, value);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return clazz.cast(value);
        } else {
            return clazz.cast(value);
        }
    }

    private <T> T asObject(Map<String, Object> map, Class<T> clazz) {
        try {
            var instance = clazz.getDeclaredConstructor().newInstance();
            map.forEach((key, value) -> {
                try {
                    var field = clazz.getDeclaredField(key);
                    field.setAccessible(true);
                    if (value instanceof Map)
                        field.set(instance, asObject((Map<String, Object>) value, field.getType()));
                    else
                        field.set(instance, value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            return clazz.cast(instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalStateException("Class " + clazz.getName() + " must have a empty constructor");
        }
    }

    private <T> boolean isPrimitive(T value){
        return value instanceof Number || value instanceof String || value instanceof Boolean || value instanceof Character;
    }

    private <T> Map<String, Object> readObject(T value) {
        var map = new HashMap<String, Object>();
        var fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                System.out.println(field.getName());
                var fieldValue = field.get(value);
                if (fieldValue != null){
                    if (isPrimitive(value))
                        map.put(field.getName(), fieldValue);
                    else {
                        map.put(field.getName(), readObject(fieldValue));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }
}
