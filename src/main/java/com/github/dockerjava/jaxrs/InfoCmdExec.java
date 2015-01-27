package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.model.Info;

public class InfoCmdExec extends AbstrDockerCmdExec<InfoCmd, Info> implements InfoCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InfoCmdExec.class);
	
	public InfoCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected Info execute(InfoCmd command) {
		Requester webResource = getBaseResource().path("/info");

		LOGGER.trace("GET: {}", webResource);
		return webResource.request().accept(Requester.MEDIA_TYPE_JSON).get(Info.class);
	}

}
