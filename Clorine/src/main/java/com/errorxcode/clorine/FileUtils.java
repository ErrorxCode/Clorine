package com.errorxcode.clorine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {

    protected static void appendLine(File file,String line){
        try {
            var writer = new FileWriter(file,true);
            writer.append(line).append("\n");
            writer.close();
            System.out.println("appended");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void updateLine(File file,String key,String value){
        try {
            var tmpFile = File.createTempFile(key,file.getName(),file.getParentFile());
            var writer = new BufferedWriter(new FileWriter(tmpFile));
            var reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                if (line.startsWith(key))
                    line = key + '=' + value;

                writer.write(line + "\n");
            }
            writer.close();
            reader.close();
            Files.delete(file.toPath());
            System.out.println(tmpFile.renameTo(file));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    protected static void removeLine(File file,String key){
        try {
            var tmpFile = File.createTempFile(file.getName() + "-","-" + key,file.getParentFile());
            var writer = new BufferedWriter(new FileWriter(tmpFile));
            var reader = new BufferedReader(new FileReader(file));
            String line;
            System.out.println("removing");
            while ((line = reader.readLine()) != null){
                System.out.println(line);
                if (!line.startsWith(key)){
                    writer.write(line + "\n");
                }
            }
            writer.close();
            reader.close();
            file.delete();
            tmpFile.renameTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected static String readLine(File file,String key){
        try {
            var reader = new BufferedReader(new FileReader(file));
            var line = reader.readLine();
            while (line != null){
                if (line.startsWith(key)){
                    reader.close();
                    return line;
                }
                line = reader.readLine();
            }
            reader.close();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
