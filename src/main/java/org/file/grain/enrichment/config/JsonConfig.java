package org.file.grain.enrichment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JsonConfig {

	@Bean
	public ObjectMapper configureJackson() {

		ObjectMapper mapper = new ObjectMapper();
		return mapper;

	}

}
