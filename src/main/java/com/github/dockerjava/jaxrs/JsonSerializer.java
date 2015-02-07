package com.github.dockerjava.jaxrs;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Singleton for ObjectMapper calls, it's a heavy object.
 */
public class JsonSerializer {
    private static JsonSerializer INSTANCE = null;
    private ObjectMapper objectMapper;
    private JsonSerializer() {
        this.objectMapper = new ObjectMapper();
    }
    public synchronized static JsonSerializer getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new JsonSerializer();
        }
        return INSTANCE;
    }

    public <T> T readValue(String body, Class<T> clazz) throws IOException {
        return objectMapper.readValue(body,clazz);
    }

    public <T> T readValue(String body, JavaType type) throws IOException {
        return objectMapper.readValue(body,type);
    }

    public <T> T readValue(JsonParser jsonParser, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonParser,clazz);
    }

    public <T> T readValue(File content, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(content,typeReference);
    }

    public String writeValueAsString(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
