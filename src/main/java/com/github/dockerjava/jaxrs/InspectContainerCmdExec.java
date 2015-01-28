package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class InspectContainerCmdExec extends AbstrDockerCmdExec<InspectContainerCmd, InspectContainerResponse> implements InspectContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InspectContainerCmdExec.class);
	
	public InspectContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected InspectContainerResponse execute(InspectContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/json").resolveTemplate("id", command.getContainerId());
		
		LOGGER.debug("GET: {}", webResource);
		return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectContainerResponse.class);
	}

}
