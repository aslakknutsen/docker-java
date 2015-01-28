package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.StartContainerCmd;

public class StartContainerCmdExec extends AbstrDockerCmdExec<StartContainerCmd, Void> implements StartContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartContainerCmdExec.class);

	public StartContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(StartContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/start").resolveTemplate("id", command.getContainerId());

		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(WebTarget.MediaType.APPLICATION_JSON).post(command, Void.class);

		return null;
	}

}
