package com.github.dockerjava.jaxrs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.model.ChangeLog;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class ContainerDiffCmdExec extends AbstrDockerCmdExec<ContainerDiffCmd, List<ChangeLog>> implements ContainerDiffCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContainerDiffCmdExec.class);
	
	public ContainerDiffCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected List<ChangeLog> execute(ContainerDiffCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/changes").resolveTemplate("id", command.getContainerId());
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.request().accept(MediaType.APPLICATION_JSON)
				.get(TypeFactory.defaultInstance().constructCollectionType(List.class, ChangeLog.class));
	}

}
