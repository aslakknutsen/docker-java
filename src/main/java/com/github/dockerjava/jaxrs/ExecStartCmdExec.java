package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.ExecStartCmd;

public class ExecStartCmdExec extends AbstrDockerCmdExec<ExecStartCmd, InputStream> implements ExecStartCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecStartCmdExec.class);

    public ExecStartCmdExec(WebTarget baseResource) {
        super(baseResource);
    }


    @Override
    protected InputStream execute(ExecStartCmd command) {
        WebTarget webResource = getBaseResource().path("/exec/{id}/start").resolveTemplate("id", command.getExecId());

        LOGGER.trace("POST: {}", webResource);

        return webResource.request().accept(WebTarget.MediaType.APPLICATION_JSON)
                .post(command, InputStream.class);
    }
}
