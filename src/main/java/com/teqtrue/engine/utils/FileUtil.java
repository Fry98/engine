package com.teqtrue.engine.utils;

import java.io.*;

public class FileUtil {

    /**
     * Serializes an object into specified file.
     * @param object an instance to save
     * @param fileName path where the object will be saved
     */
    public static void saveObject(Object object, String fileName) {
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(object);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an object deserialized from file.
     * @param fileName path from where to read
     * @param clazz type to expect in the serialized object
     */
    public static <T> T loadObject(String fileName, Class<T> clazz) {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new FileInputStream(fileName));
            T sz = clazz.cast(in.readObject());
            in.close();                
            return sz;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
