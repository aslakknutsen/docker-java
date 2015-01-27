package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.RemoveImageCmd;

public class RemoveImageCmdExec extends AbstrDockerCmdExec<RemoveImageCmd, Void> implements RemoveImageCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoveImageCmdExec.class);

	public RemoveImageCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(RemoveImageCmd command) {
		Requester webResource = getBaseResource().path("/images/" + command.getImageId())
				.queryParam("force", command.hasForceEnabled() ? "1" : "0")
				.queryParam("noprune", command.hasNoPruneEnabled() ? "1" : "0");

		LOGGER.trace("DELETE: {}", webResource);
		webResource.request().delete();
		
		return null;
	}

}
