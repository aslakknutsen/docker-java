package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class WaitContainerCmdExec extends AbstrDockerCmdExec<WaitContainerCmd, Integer> implements WaitContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WaitContainerCmdExec.class);

	public WaitContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Integer execute(WaitContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/wait")
				.resolveTemplate("id", command.getContainerId());

		LOGGER.trace("POST: {}", webResource);
		ObjectNode ObjectNode = webResource.request().accept(MediaType.APPLICATION_JSON)
				.post(ObjectNode.class);
		
        return ObjectNode.get("StatusCode").asInt();
	}

}
