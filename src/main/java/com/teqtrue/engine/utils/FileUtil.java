package com.teqtrue.engine.utils;

import java.io.*;

public class FileUtil {

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
