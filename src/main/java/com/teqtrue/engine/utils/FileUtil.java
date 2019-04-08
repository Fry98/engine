package com.teqtrue.engine.utils;

import java.io.*;

public class FileUtil {

    public static void saveObject(Object object, String fileName) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(object);
        out.close();
    }

    public static <T> T loadObject(String fileName, Class<T> clazz) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
        T sz = clazz.cast(in.readObject());
        in.close();
        return sz;
    }

}
