package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.model.AuthResponse;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class AuthCmdExec extends AbstrDockerCmdExec<AuthCmd,AuthResponse> implements AuthCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthCmdExec.class);
	
	public AuthCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected AuthResponse execute(AuthCmd command) {
		WebTarget webResource = getBaseResource().path("/auth");
		LOGGER.trace("POST: {}", webResource);
		return webResource.request()
                .accept(MediaType.APPLICATION_JSON).post(command.getAuthConfig(), AuthResponse.class);
	}

}
