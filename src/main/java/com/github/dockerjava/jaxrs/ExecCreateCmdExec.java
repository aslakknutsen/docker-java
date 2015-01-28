package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class ExecCreateCmdExec extends AbstrDockerCmdExec<ExecCreateCmd, ExecCreateCmdResponse> implements ExecCreateCmd.Exec {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(VersionCmdExec.class);

    public ExecCreateCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected ExecCreateCmdResponse execute(ExecCreateCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/exec").resolveTemplate("id", command.getContainerId());

        LOGGER.trace("POST: {}", webResource);

        return webResource.request().accept(MediaType.APPLICATION_JSON).post(command, ExecCreateCmdResponse.class);
    }
}
