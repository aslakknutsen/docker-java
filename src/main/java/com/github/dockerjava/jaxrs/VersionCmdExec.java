package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.model.Version;

public class VersionCmdExec extends AbstrDockerCmdExec<VersionCmd, Version> implements VersionCmd.Exec {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(VersionCmdExec.class);

	public VersionCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected Version execute(VersionCmd command) {
		Requester webResource = getBaseResource().path("/version");

		LOGGER.trace("GET: {}", webResource);
		return webResource.request().accept(Requester.MEDIA_TYPE_JSON)
				.get(Version.class);
	}

}
