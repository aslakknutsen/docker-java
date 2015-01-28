package com.github.dockerjava.jaxrs;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.dockerjava.jaxrs.util.ResponseStatusExceptionFilter;

public class WebTarget {

    public enum MediaType {
        APPLICATION_JSON("application/json"),
        APPLICATION_OCTET_STREAM("application/octet-stream"),
        APPLICATION_TAR("application/tar"),
        TEXT_PLAIN("text/plain");

        private String type;
        private MediaType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
            
    private URIBuilder root;
    private CloseableHttpClient client;
    private HashMap<String, String> subsitution;
    
    public static WebTarget from(CloseableHttpClient client, URIBuilder root) {
        return new WebTarget(client, root, new HashMap<String, String>());
    }
    
    private WebTarget(CloseableHttpClient client, URIBuilder root, HashMap<String, String> subsitution) {
        this.client = client;
        this.root = root;
        this.subsitution = subsitution;
    }
    
    public WebTarget path(String path) {
        URIBuilder newRoot = cloneRoot();
        newRoot.setPath(newRoot.getPath() +  path);
        return new WebTarget(client, newRoot, subsitution);
    }
    
    public WebTarget queryParam(String key, String value) {
        URIBuilder newRoot = cloneRoot();
        newRoot.addParameter(key, value);
        return new WebTarget(client, newRoot, subsitution);
    }
    
    public WebTarget resolveTemplate(String key, String value) {
        HashMap<String, String> newSubstritution = new HashMap<String, String>(subsitution);
        newSubstritution.put(key, value);
        return new WebTarget(client, root, newSubstritution);
    }
    
    private URIBuilder cloneRoot() {
        return new URIBuilder(URI.create(root.toString()));
    }
    
    public Request request() {
        return new Request();
    }
    
    public class Request {
        private Map<String, String> headers;

        public Request() {
            this.headers = new HashMap<String, String>();
        }

        public Request accept(MediaType mediaType) {
            return accept(mediaType.getType());
        }

        public Request accept(String mediaType) {
            return header(HttpHeaders.ACCEPT, mediaType);
        }
        
        public Request contentType(MediaType mediaType) {
            return contentType(mediaType.getType());
        }

        public Request contentType(String mediaType) {
            return header(HttpHeaders.CONTENT_TYPE, mediaType);
        }

        public Request header(String header, String value) {
            this.headers.put(header, value);
            return this;
        }

        public <T> T get(Class<T> type) {
            RequestBuilder builder = setupBuilder(RequestBuilder.get());
            CloseableHttpResponse response = null;
            try {
                System.out.println(builder.getMethod() + ": " + builder.getUri());
                response = client.execute(builder.build());
                checkResponse(response);

                // hack
                if(type == InputStream.class) {
                    return (T)new ResponseClosableInputStream(response, response.getEntity().getContent());
                }
                if(type == Void.class) {
                    closeResponse(response);
                    return (T) null;
                }

                ObjectMapper mapper = new ObjectMapper(); 
                String responseEntity = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseEntity);

                closeResponse(response);
                return mapper.readValue(responseEntity, type);
            } catch (Exception e) {
                closeResponse(response);
                throw new RuntimeException(e);
            }
        }

        public <T> T get(CollectionType type) {
            RequestBuilder builder = setupBuilder(RequestBuilder.get());
            CloseableHttpResponse response = null;
            try {
                System.out.println(builder.getMethod() + ": " + builder.getUri());
                response = client.execute(builder.build());
                checkResponse(response);

                ObjectMapper mapper = new ObjectMapper();
                
                String responseEntity = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseEntity);

                closeResponse(response);
                return mapper.readValue(responseEntity, type);
            } catch (Exception e) {
                closeResponse(response);
                throw new RuntimeException(e);
            }
        }

        public <T> T post(Object obj, Class<T> type) {
            if(obj instanceof InputStream) {
                return post(obj, MediaType.APPLICATION_OCTET_STREAM, type);
            } else {
                return post(obj, MediaType.APPLICATION_JSON, type);
            }
        }

        public <T> T post(Object obj, MediaType mediaType, Class<T> type) {
            return post(obj, mediaType.getType(), type);
        }

        public <T> T post(Object obj, String mediaType, Class<T> type) {
            ObjectMapper mapper = new ObjectMapper();
            RequestBuilder builder = setupBuilder(RequestBuilder.post());
            CloseableHttpResponse response = null;
            try {
                System.out.println(builder.getMethod() + ": " + builder.getUri());
                builder.addHeader(HttpHeaders.CONTENT_TYPE, mediaType);
                if(obj instanceof InputStream) {
                    builder.setEntity(new InputStreamEntity((InputStream)obj));
                } else {
                    if(obj != null) {
                        String requestEntity = mapper.writeValueAsString(obj);
                        System.out.println("Request: " + requestEntity);
                        builder.setEntity(new StringEntity(requestEntity, "UTF-8"));
                    }
                }
                response = client.execute(builder.build());
                checkResponse(response);

                // hack
                if(type == InputStream.class) {
                    return (T)new ResponseClosableInputStream(response, response.getEntity().getContent());
                }
                if(type == Void.class) {
                    closeResponse(response);
                    return (T) null;
                }
                
                String responseEntity = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseEntity);
                
                closeResponse(response);
                return mapper.readValue(responseEntity, type);
            } catch (Exception e) {
                closeResponse(response);
                throw new RuntimeException(e);
            }
        }
        
        public void delete() {
            RequestBuilder builder = setupBuilder(RequestBuilder.delete());
            CloseableHttpResponse response = null;
            try {
                response = client.execute(builder.build());
                checkResponse(response);
                
            } catch (Exception e) {
                closeResponse(response);
                throw new RuntimeException(e);
            }
        }

        private void closeResponse(CloseableHttpResponse response) {
            if(response == null) {
                return;
            }
            try {
                System.out.println("Closing");
                ((Closeable)response).close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void checkResponse(CloseableHttpResponse response) {
            boolean thrownException = false;
            try {
                ResponseStatusExceptionFilter.convertReturnCode(response);
            } catch(RuntimeException e){
                thrownException = true;
                throw e;
            } finally {
                if(thrownException) {
                    closeResponse(response);
                }
            }
        }
        
        private RequestBuilder setupBuilder(RequestBuilder builder) {
            builder.setUri(replace(root.toString(), subsitution));
            for(Map.Entry<String, String> entry : this.headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
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
        
        private class ResponseClosableInputStream extends InputStream {
            private CloseableHttpResponse response;
            private InputStream delegate;
            
            public ResponseClosableInputStream(CloseableHttpResponse response, InputStream delegate) {
                this.response = response;
                this.delegate = delegate;
            }
            
            public int read() throws IOException {
                return delegate.read();
            }
            public int hashCode() {
                return delegate.hashCode();
            }
            public int read(byte[] b) throws IOException {
                return delegate.read(b);
            }
            public boolean equals(Object obj) {
                return delegate.equals(obj);
            }
            public int read(byte[] b, int off, int len) throws IOException {
                return delegate.read(b, off, len);
            }
            public long skip(long n) throws IOException {
                return delegate.skip(n);
            }
            public String toString() {
                return delegate.toString();
            }
            public int available() throws IOException {
                return delegate.available();
            }
            public void close() throws IOException {
                System.out.println("Closing");
                try {
                    response.close();
                } finally {
                    delegate.close();
                }
            }
            public void mark(int readlimit) {
                delegate.mark(readlimit);
            }
            public void reset() throws IOException {
                delegate.reset();
            }
            public boolean markSupported() {
                return delegate.markSupported();
            }
        }
    }
    
}
