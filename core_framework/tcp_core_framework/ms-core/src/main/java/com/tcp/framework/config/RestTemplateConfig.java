package com.tcp.framework.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

/**
 * 
 * creating a rest template for calling external SFCC microservices.
 *
 */
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

	/**
	 * 
	 * @param restTemplateBuilder
	 * @return
	 * 
	 *         create a request template Bean.
	 */
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

		return restTemplateBuilder.build();
	}

}
