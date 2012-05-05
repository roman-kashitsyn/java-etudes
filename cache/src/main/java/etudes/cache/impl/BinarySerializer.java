package com.wiley.cache.impl;

import com.wiley.cache.SerializationException;
import com.wiley.cache.Serializer;

import java.io.*;

public class BinarySerializer implements Serializer {

    public void serialize(Object object, OutputStream outputStream) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
        } catch (IOException ioe) {
            throw  new SerializationException("Unable to save object to stream", ioe);
        }
    }

    public Object deserialize(InputStream inputStream) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            throw new SerializationException("Unable to read object from stream", e);
        }
    }
}
