package com.github.dockerjava.jaxrs;

import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Requester {

    public static final String MEDIA_TYPE_JSON = "application/json";
    
    private URIBuilder root;
    private HttpClient client;
    
    public static Requester from(HttpClient client, URIBuilder root) {
        return new Requester(client, root);
    }
    
    private Requester(HttpClient client, URIBuilder root) {
        this.client = client;
        this.root = root;
    }
    
    public Requester path(String path) {
        URIBuilder newRoot = cloneRoot();
        newRoot.setPath(newRoot.getPath() +  path);
        return new Requester(client, newRoot);
    }
    
    public Requester queryParam(String key, String value) {
        URIBuilder newRoot = cloneRoot();
        newRoot.addParameter(key, value);
        return new Requester(client, newRoot);
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
            RequestBuilder builder = createBuilder();
            try {
                HttpResponse response = client.execute(builder.build());
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.getEntity().getContent(), type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        public <T> T post(Object obj, Class<T> type) {
            ObjectMapper mapper = new ObjectMapper();
            RequestBuilder builder = createBuilder();
            if(builder.getHeaders(HttpHeaders.CONTENT_TYPE).length == 0) {
                builder.addHeader(HttpHeaders.CONTENT_TYPE, accept);
            }
            try {
                builder.setEntity(new StringEntity(mapper.writeValueAsString(obj), "UTF-8"));
                HttpResponse response = client.execute(builder.build());
                
                String body = IOUtils.toString(response.getEntity().getContent());
                System.out.println(body);
                return mapper.readValue(body, type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        private RequestBuilder createBuilder() {
            RequestBuilder builder = RequestBuilder.get();
            builder.setUri(root.toString());
            if(accept != null) {
                builder.addHeader(HttpHeaders.ACCEPT, accept);
            }
            if(contentType != null) {
                builder.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
            }
            return builder;
        }
    }
    
}
