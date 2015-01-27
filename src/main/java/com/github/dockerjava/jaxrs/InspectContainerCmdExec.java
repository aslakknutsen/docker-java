package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class InspectContainerCmdExec extends AbstrDockerCmdExec<InspectContainerCmd, InspectContainerResponse> implements InspectContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InspectContainerCmdExec.class);
	
	public InspectContainerCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected InspectContainerResponse execute(InspectContainerCmd command) {
		Requester webResource = getBaseResource().path("/containers/{id}/json").resolveTemplate("id", command.getContainerId());
		
		LOGGER.debug("GET: {}", webResource);
		return webResource.request().accept(Requester.MEDIA_TYPE_JSON).get(InspectContainerResponse.class);
	}

}
