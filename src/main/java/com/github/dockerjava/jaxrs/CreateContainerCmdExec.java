package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;

public class CreateContainerCmdExec extends AbstrDockerCmdExec<CreateContainerCmd, CreateContainerResponse> implements CreateContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateContainerCmdExec.class);
	
	public CreateContainerCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected CreateContainerResponse execute(CreateContainerCmd command) {
		Requester webResource = getBaseResource().path("/containers/create");

        if (command.getName() != null) {
            webResource = webResource.queryParam("name", command.getName());
        }

		LOGGER.trace("POST: {} ", webResource);
		return webResource.request().accept(Requester.MEDIA_TYPE_JSON)
				.post(command, CreateContainerResponse.class);
	}

}
