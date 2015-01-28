package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.model.AuthResponse;

public class AuthCmdExec extends AbstrDockerCmdExec<AuthCmd,AuthResponse> implements AuthCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthCmdExec.class);
	
	public AuthCmdExec(Requester baseResource) {
		super(baseResource);
	}

	@Override
	protected AuthResponse execute(AuthCmd command) {
		Requester webResource = getBaseResource().path("/auth");
		LOGGER.trace("POST: {}", webResource);
		return webResource.request()
                .accept(Requester.MEDIA_TYPE_JSON).post(command.getAuthConfig(), AuthResponse.class);
	}

}
