package com.github.dockerjava.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.command.CommitCmd;

public class CommitCmdExec extends AbstrDockerCmdExec<CommitCmd, String> implements CommitCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CommitCmdExec.class);
	
	public CommitCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected String execute(CommitCmd command) {
		WebTarget webResource = getBaseResource().path("/commit")
                .queryParam("container", command.getContainerId())
                .queryParam("repo", command.getRepository())
                .queryParam("tag", command.getTag())
                .queryParam("m", command.getMessage())
                .queryParam("author", command.getAuthor())
                .queryParam("pause",  command.hasPauseEnabled() ? "1" : "0");
		
		LOGGER.trace("POST: {}", webResource);
		ObjectNode objectNode = webResource.request().accept("application/vnd.docker.raw-stream").post(command, ObjectNode.class);
        return objectNode.get("Id").asText();
	}

}
