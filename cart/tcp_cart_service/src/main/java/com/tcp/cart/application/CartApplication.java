package com.tcp.cart.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;

/**
 * Main entry point for the cart application
 */
@SpringBootApplication(exclude = { CassandraAutoConfiguration.class }, scanBasePackages = { "com.tcp" })
public class CartApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartApplication.class, args);
	}
}
