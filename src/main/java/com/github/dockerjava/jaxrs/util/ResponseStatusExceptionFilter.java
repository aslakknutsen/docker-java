package com.github.dockerjava.jaxrs.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import com.github.dockerjava.api.BadRequestException;
import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotAcceptableException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.UnauthorizedException;

/**
 * This {@link ClientResponseFilter} implementation detects http status codes and throws {@link DockerException}s 
 *
 * @author marcus
 *
 */
public class ResponseStatusExceptionFilter { // implements ClientResponseFilter {


   // @Override
    //public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
    public static void convertReturnCode(HttpResponse response) {
		int status = response.getStatusLine().getStatusCode();
        switch (status) {
		case 200:
		case 201:	
		case 204:
			return;
		case 304:
			throw new NotModifiedException(getBodyAsMessage(response));
		case 400:
			throw new BadRequestException(getBodyAsMessage(response));
		case 401:
			throw new UnauthorizedException(getBodyAsMessage(response));	
		case 404:
			throw new NotFoundException(getBodyAsMessage(response));
		case 406:
			throw new NotAcceptableException(getBodyAsMessage(response));
		case 409:
			throw new ConflictException(getBodyAsMessage(response));
		case 500:
			throw new InternalServerErrorException(getBodyAsMessage(response));
		default:
			throw new DockerException(getBodyAsMessage(response), status);
		}
    }
    
    public static String getBodyAsMessage(HttpResponse response) {
        try {
            return IOUtils.toString(response.getEntity().getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
/*
	public String getBodyAsMessage(ClientResponseContext responseContext)
			throws IOException {
	    if (responseContext.hasEntity()) {
	        int contentLength = responseContext.getLength();
	        if (contentLength != -1) {
	            byte[] buffer = new byte[contentLength];
	            try {
	                InputStream entityStream = responseContext.getEntityStream();
                  IOUtils.readFully(entityStream, buffer);
                  entityStream.close();
	            }
	            catch (EOFException e) {
	                return null;
	            }
	            Charset charset = null;
	            MediaType mediaType = responseContext.getMediaType();
	            if (mediaType != null) {
	                String charsetName = mediaType.getParameters().get("charset");
	                if (charsetName != null) {
	                    try {
	                        charset = Charset.forName(charsetName);
                        }
                        catch (Exception e) {
                            //Do noting...
                        }
	                }
	            }
	            if (charset == null) {
                    charset = Charset.defaultCharset();
	            }
	            String message = new String(buffer, charset);
	            return message;
	        }
	    }
	    return null;
	}
*/
}
