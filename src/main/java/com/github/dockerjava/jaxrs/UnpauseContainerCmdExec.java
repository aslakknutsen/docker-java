package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.UnpauseContainerCmd;

public class UnpauseContainerCmdExec extends AbstrDockerCmdExec<UnpauseContainerCmd, Void> implements UnpauseContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnpauseContainerCmdExec.class);

	public UnpauseContainerCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(UnpauseContainerCmd command) {
		Requester webResource = getBaseResource().path("/containers/{id}/unpause")
				.resolveTemplate("id", command.getContainerId());
		
		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(Requester.MEDIA_TYPE_JSON)
				.post(null, Void.class);

		return null;
	}

}
