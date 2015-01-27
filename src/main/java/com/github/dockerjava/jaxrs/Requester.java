package com.github.dockerjava.jaxrs;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class Requester {

    public static final String MEDIA_TYPE_JSON = "application/json";
    
    private URIBuilder root;
    private HttpClient client;
    private HashMap<String, String> subsitution;
    
    public static Requester from(HttpClient client, URIBuilder root) {
        return new Requester(client, root, new HashMap<String, String>());
    }
    
    private Requester(HttpClient client, URIBuilder root, HashMap<String, String> subsitution) {
        this.client = client;
        this.root = root;
        this.subsitution = subsitution;
    }
    
    public Requester path(String path) {
        URIBuilder newRoot = cloneRoot();
        newRoot.setPath(newRoot.getPath() +  path);
        return new Requester(client, newRoot, subsitution);
    }
    
    public Requester queryParam(String key, String value) {
        URIBuilder newRoot = cloneRoot();
        newRoot.addParameter(key, value);
        return new Requester(client, newRoot, subsitution);
    }
    
    public Requester resolveTemplate(String key, String value) {
        HashMap<String, String> newSubstritution = (HashMap<String, String>)subsitution.clone();
        newSubstritution.put(key, value);
        return new Requester(client, root, newSubstritution);
    }
    
    private URIBuilder cloneRoot() {
        return new URIBuilder(URI.create(root.toString()));
    }
    
    public Request request() {
        return new Request();
    }
    
    public class Request {
        private String accept = null;
        private String contentType = null;

        public Request() {
        }

        public Request accept(String mediaType) {
            this.accept = mediaType;
            return this;
        }
        
        public Request contentType(String mediaType) {
            this.contentType = mediaType;
            return this;
        }

        public <T> T get(Class<T> type) {
            RequestBuilder builder = setupBuilder(RequestBuilder.get());
            try {
                System.out.println("URI: " + builder.getUri());
                HttpResponse response = client.execute(builder.build());
                ObjectMapper mapper = new ObjectMapper();

                String responseEntity = IOUtils.toString(response.getEntity().getContent());
                System.out.println("Response: " + responseEntity);
                return mapper.readValue(responseEntity, type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        public <T> T get(CollectionType type) {
            RequestBuilder builder = setupBuilder(RequestBuilder.get());
            try {
                System.out.println("URI: " + builder.getUri());
                HttpResponse response = client.execute(builder.build());
                ObjectMapper mapper = new ObjectMapper();

                String responseEntity = IOUtils.toString(response.getEntity().getContent());
                System.out.println("Response: " + responseEntity);
                return mapper.readValue(responseEntity, type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public <T> T post(Object obj, Class<T> type) {
            ObjectMapper mapper = new ObjectMapper();
            RequestBuilder builder = setupBuilder(RequestBuilder.post());
            if(builder.getHeaders(HttpHeaders.CONTENT_TYPE).length == 0) {
                builder.addHeader(HttpHeaders.CONTENT_TYPE, accept);
            }
            try {
                String requestEntity = mapper.writeValueAsString(obj);
                System.out.println("URI: " + builder.getUri());
                System.out.println("Request: " + requestEntity);
                builder.setEntity(new StringEntity(requestEntity, "UTF-8"));
                HttpResponse response = client.execute(builder.build());

                // hack
                if(type == InputStream.class) {
                    return (T)response.getEntity().getContent();
                }
                if(type == Void.class) {
                    return (T) null;
                }
                String responseEntity = IOUtils.toString(response.getEntity().getContent());
                System.out.println("Response: " + responseEntity);
                return mapper.readValue(responseEntity, type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        public void delete() {
            RequestBuilder builder = setupBuilder(RequestBuilder.delete());
            if(builder.getHeaders(HttpHeaders.CONTENT_TYPE).length == 0) {
                builder.addHeader(HttpHeaders.CONTENT_TYPE, accept);
            }
            try {
                client.execute(builder.build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        private RequestBuilder setupBuilder(RequestBuilder builder) {
            builder.setUri(replace(root.toString(), subsitution));
            if(accept != null) {
                builder.addHeader(HttpHeaders.ACCEPT, accept);
            }
            if(contentType != null) {
                builder.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
            }
            return builder;
        }
        
        private String replace(String source, Map<String, String> values) {
            String target = source;
            for(Map.Entry<String, String> entry : values.entrySet()) {
                target = target.replaceAll("%7B" + entry.getKey() + "%7D", entry.getValue());
            }
            return target;
        }
    }
    
}
