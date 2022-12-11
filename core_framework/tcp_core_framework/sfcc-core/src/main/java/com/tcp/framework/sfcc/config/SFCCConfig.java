package com.tcp.framework.sfcc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Responsible for fetching all configuration from centralized config server.
 */
@Configuration
@RefreshScope
@Data
public class SFCCConfig {

	@Value("${SFCC.core.url.tokenUrl}")
	public String tokenUrl;

	@Value("${SFCC.auth.username}")
	public String userName;

	@Value("${SFCC.auth.password}")
	public String password;

}