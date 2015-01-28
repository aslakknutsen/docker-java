package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateImageResponse;

public class CreateImageCmdExec extends AbstrDockerCmdExec<CreateImageCmd, CreateImageResponse> implements CreateImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CreateImageCmdExec.class);
	
	public CreateImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected CreateImageResponse execute(CreateImageCmd command) {
		WebTarget webResource = getBaseResource()
                .path("/images/create")
                .queryParam("repo", command.getRepository())
                .queryParam("tag", command.getTag())
                .queryParam("fromSrc", "-");
		
		LOGGER.trace("POST: {}", webResource);
		return webResource.request().accept(WebTarget.MediaType.APPLICATION_OCTET_STREAM)
				.post(command.getImageStream(), CreateImageResponse.class);
	}
}
