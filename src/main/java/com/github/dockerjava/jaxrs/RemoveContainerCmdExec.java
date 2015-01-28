package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.RemoveContainerCmd;

public class RemoveContainerCmdExec extends AbstrDockerCmdExec<RemoveContainerCmd, Void> implements RemoveContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoveContainerCmdExec.class);

	public RemoveContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(RemoveContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/" + command.getContainerId())
				.queryParam("v", command.hasRemoveVolumesEnabled() ? "1" : "0")
				.queryParam("force", command.hasForceEnabled() ? "1" : "0");
		
		LOGGER.trace("DELETE: {}", webResource);
		/*String response = */
		webResource.request().accept(WebTarget.MediaType.APPLICATION_JSON).delete();
//		LOGGER.trace("Response: {}", response);

		return null;
	}

}
