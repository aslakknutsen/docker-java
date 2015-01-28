package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class CopyFileFromContainerCmdExec extends AbstrDockerCmdExec<CopyFileFromContainerCmd, InputStream> implements CopyFileFromContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CopyFileFromContainerCmdExec.class);
	
	public CopyFileFromContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(CopyFileFromContainerCmd command) {
		WebTarget webResource = getBaseResource()
				.path("/containers/{id}/copy")
				.resolveTemplate("id", command.getContainerId());

		LOGGER.trace("POST: " + webResource.toString());
		
		return webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM).post(command, InputStream.class);
	}

}
