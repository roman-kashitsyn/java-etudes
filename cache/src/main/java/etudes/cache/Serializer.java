package com.wiley.cache;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {
    
    void serialize(Object object, OutputStream outputStream);
    
    Object deserialize(InputStream inputStream);
}
