package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.KillContainerCmd;

public class KillContainerCmdExec extends AbstrDockerCmdExec<KillContainerCmd, Void> implements KillContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(KillContainerCmdExec.class);
	
	public KillContainerCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(KillContainerCmd command) {
		Requester webResource = getBaseResource().path("/containers/{id}/kill").resolveTemplate("id", command.getContainerId());

		if(command.getSignal() != null) {
			webResource = webResource.queryParam("signal", command.getSignal());
		}
	
		LOGGER.trace("POST: {}", webResource);
		webResource.request().accept(Requester.MEDIA_TYPE_JSON).post(null, Void.class);	

		return null;
	}

}
