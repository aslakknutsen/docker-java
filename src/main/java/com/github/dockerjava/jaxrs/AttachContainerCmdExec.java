package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class AttachContainerCmdExec extends AbstrDockerCmdExec<AttachContainerCmd, InputStream> implements AttachContainerCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AttachContainerCmdExec.class);
	
	public AttachContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(AttachContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/attach")
                .resolveTemplate("id", command.getContainerId())
                .queryParam("logs", command.hasLogsEnabled() ? "1" : "0")
             // .queryParam("stdin", command.hasStdinEnabled() ? "1" : "0")
                .queryParam("stdout", command.hasStdoutEnabled() ? "1" : "0")
                .queryParam("stderr", command.hasStderrEnabled() ? "1" : "0")
                .queryParam("stream", command.hasFollowStreamEnabled() ? "1" : "0");

		LOGGER.trace("POST: {}", webResource);
		
		return webResource.request().accept(MediaType.APPLICATION_JSON)
				.post(InputStream.class);
	}

}
