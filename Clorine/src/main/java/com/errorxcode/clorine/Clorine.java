package com.errorxcode.clorine;

import java.io.File;
import java.io.IOException;

/**
 * A class that contains static methods to use this library easily
 */
public class Clorine {

    /**
     * Create a new cache or open an existing cache
     * @param capacity the maximum number of objects that can be stored in the cache
     * @param name the name of the cache
     * @param directory the directory where the cache will be stored
     * @return the cache
     */
    public static Cache createOrOpen(int capacity,String name,File directory){
        var cacheDir = new File(directory,name);
        if (cacheDir.mkdir())
            return new Cache(capacity,cacheDir);
        else
            throw new RuntimeException("You might have already created a cache with this name or do not have permission to create a cache in this directory");
    }
    
    /**
     * Delete a cache
     * @param name the name of the cache
     * @param directory the directory where the cache is stored
     */
    public static void delete(String name,File directory){
        new File(directory,name).delete();
    }
}
