package com.github.dockerjava.jaxrs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class SearchImagesCmdExec extends AbstrDockerCmdExec<SearchImagesCmd, List<SearchItem>> implements SearchImagesCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchImagesCmdExec.class);
	
	public SearchImagesCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected List<SearchItem> execute(SearchImagesCmd command) {
		WebTarget webResource = getBaseResource().path("/images/search").queryParam("term", command.getTerm());
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.request().accept(MediaType.APPLICATION_JSON)
		        .get(TypeFactory.defaultInstance().constructCollectionType(List.class, SearchItem.class));
	}

}
