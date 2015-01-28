package com.github.dockerjava.jaxrs;

import static com.github.dockerjava.jaxrs.util.guava.Guava.urlPathSegmentEscaper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.jaxrs.WebTarget.MediaType;

public class ListImagesCmdExec extends
		AbstrDockerCmdExec<ListImagesCmd, List<Image>> implements
		ListImagesCmd.Exec {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ListImagesCmdExec.class);

	public ListImagesCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected List<Image> execute(ListImagesCmd command) {
		WebTarget webResource = getBaseResource().path("/images/json")
				.queryParam("all", command.hasShowAllEnabled() ? "1" : "0");

		if (command.getFilters() != null)
			webResource = webResource.queryParam("filters",
					urlPathSegmentEscaper().escape(command.getFilters()));

		LOGGER.trace("GET: {}", webResource);

		List<Image> images = webResource.request()
				.accept(MediaType.APPLICATION_JSON)
				.get(TypeFactory.defaultInstance().constructCollectionType(List.class, Image.class));
		LOGGER.trace("Response: {}", images);

		return images;
	}

}
