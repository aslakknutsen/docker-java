package com.github.dockerjava.jaxrs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class ListContainersCmdExec extends AbstrDockerCmdExec<ListContainersCmd, List<Container>> implements ListContainersCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ListContainersCmdExec.class);
	
	public ListContainersCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected List<Container> execute(ListContainersCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/json")
                .queryParam("all", command.hasShowAllEnabled() ? "1" : "0")
                .queryParam("since", command.getSinceId())
                .queryParam("before", command.getBeforeId())
                .queryParam("size", command.hasShowSizeEnabled() ? "1" : "0");

        if (command.getLimit() >= 0) {
            webResource = webResource.queryParam("limit", String.valueOf(command.getLimit()));
        }

		LOGGER.trace("GET: {}", webResource);
		List<Container> containers = webResource.request().accept(MediaType.APPLICATION_JSON)
		        .get(TypeFactory.defaultInstance().constructCollectionType(List.class, Container.class));
		LOGGER.trace("Response: {}", containers);

		return containers;
	}

}
