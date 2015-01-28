package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.RestartContainerCmd;

public class RestartContainerCmdExec extends AbstrDockerCmdExec<RestartContainerCmd, Void> implements RestartContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestartContainerCmdExec.class);

	public RestartContainerCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(RestartContainerCmd command) {
		Requester webResource = getBaseResource().path("/containers/{id}/restart")
				.resolveTemplate("id", command.getContainerId())
				.queryParam("t", String.valueOf(command.getTimeout()));
		
		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(Requester.MEDIA_TYPE_JSON).post(null, Void.class);

		return null;
	}

}
