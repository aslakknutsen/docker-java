package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PauseContainerCmd;

public class PauseContainerCmdExec extends AbstrDockerCmdExec<PauseContainerCmd, Void> implements PauseContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(PauseContainerCmdExec.class);

	public PauseContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(PauseContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/pause")
				.resolveTemplate("id", command.getContainerId());
		
		LOGGER.trace("POST: {}", webResource);
		webResource.request()
				.accept(WebTarget.MediaType.APPLICATION_JSON)
				.post();

		return null;
	}

}
