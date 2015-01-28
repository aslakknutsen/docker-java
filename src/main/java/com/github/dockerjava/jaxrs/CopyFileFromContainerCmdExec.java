package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CopyFileFromContainerCmd;

public class CopyFileFromContainerCmdExec extends AbstrDockerCmdExec<CopyFileFromContainerCmd, InputStream> implements CopyFileFromContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CopyFileFromContainerCmdExec.class);
	
	public CopyFileFromContainerCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(CopyFileFromContainerCmd command) {
		Requester webResource = getBaseResource()
				.path("/containers/{id}/copy")
				.resolveTemplate("id", command.getContainerId());

		LOGGER.trace("POST: " + webResource.toString());
		
		return webResource.request().accept(Requester.MEDIA_TYPE_OCTET_STREAM).post(command, InputStream.class);		
	}

}
