package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.StartContainerCmd;

public class StartContainerCmdExec extends AbstrDockerCmdExec<StartContainerCmd, Void> implements StartContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartContainerCmdExec.class);

	public StartContainerCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(StartContainerCmd command) {
		Requester webResource = getBaseResource().path("/containers/{id}/start").resolveTemplate("id", command.getContainerId());

		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(Requester.MEDIA_TYPE_JSON).post(command, Void.class);

		return null;
	}

}
