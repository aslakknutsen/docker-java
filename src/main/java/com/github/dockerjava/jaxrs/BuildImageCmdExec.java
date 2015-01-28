package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class BuildImageCmdExec extends AbstrDockerCmdExec<BuildImageCmd, InputStream> implements BuildImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BuildImageCmdExec.class);
	
	public BuildImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(BuildImageCmd command) {
		WebTarget webResource = getBaseResource().path("/build");
		
		if(command.getTag() != null) {
			webResource = webResource.queryParam("t", command.getTag());
		}
        if (command.hasNoCacheEnabled()) {
            webResource = webResource.queryParam("nocache", "true");
        }
        if (command.hasRemoveEnabled()) {
            webResource = webResource.queryParam("rm", "true");
        }
        if (command.isQuiet()) {
            webResource = webResource.queryParam("q", "true");
        }
		

	  //webResource.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.CHUNKED);
	  //webResource.property(ClientProperties.CHUNKED_ENCODING_SIZE, 1024*1024);

		LOGGER.debug("POST: {}", webResource);
		return webResource
                .request()
				.accept(MediaType.TEXT_PLAIN)
				.post(command.getTarInputStream(), MediaType.APPLICATION_TAR, InputStream.class);
		
	}

}
