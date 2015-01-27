package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.StopContainerCmd;

public class StopContainerCmdExec extends AbstrDockerCmdExec<StopContainerCmd, Void> implements StopContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(StopContainerCmdExec.class);

	public StopContainerCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(StopContainerCmd command) {
		Requester webResource = getBaseResource().path("/containers/{id}/stop")
				.resolveTemplate("id", command.getContainerId())
				.queryParam("t", String.valueOf(command.getTimeout()));
		
		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(Requester.MEDIA_TYPE_JSON).post(null, Void.class);

		return null;
	}
}
