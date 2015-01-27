package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;

public class InspectImageCmdExec extends AbstrDockerCmdExec<InspectImageCmd, InspectImageResponse> implements InspectImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InspectImageCmdExec.class);
	
	public InspectImageCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected InspectImageResponse execute(InspectImageCmd command) {
		Requester webResource = getBaseResource().path("/images/{id}/json").resolveTemplate("id", command.getImageId());

		LOGGER.trace("GET: {}", webResource);
		return webResource.request().accept(Requester.MEDIA_TYPE_JSON).get(InspectImageResponse.class);
	}

}
