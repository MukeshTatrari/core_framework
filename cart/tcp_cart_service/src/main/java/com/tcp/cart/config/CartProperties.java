package com.tcp.cart.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Responsible for fetching all configuration from centralized config server.
 */
@Configuration
@RefreshScope
@Data
public class CartProperties {

	@Value("${product.name}")
	private String productName;

	@Value("${SFCC.cart.url.tokenUri}")
	String tokenUri;
	
	@Value("${SFCC.cart.url.createCartUri}")
	String createCartUri;
	
	@Value("${SFCC.cart.url.getCartUri}")
	String getCartUri;

	@Value("${SFCC.cart.url.baseUrl}")
	String baseUrl;
	
	@Value("${SFCC.cart.url.addItemsUri}")
	String addItemsUri;
	
	@Value("${SFCC.auth.username}")
	String userName;
	
	@Value("${SFCC.auth.password}")
	String password;
	
}
