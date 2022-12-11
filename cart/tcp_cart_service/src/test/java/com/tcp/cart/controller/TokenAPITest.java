package com.tcp.cart.controller;

import static com.tcp.cart.constants.CartTestConstants.API_ENDPOINT_TOKEN;
import static com.tcp.cart.constants.CartTestConstants.API_HEADER_PASSWORD;
import static com.tcp.cart.constants.CartTestConstants.API_HEADER_USERNAME;
import static com.tcp.cart.constants.CartTestConstants.CREDENTIAL_TYPE_CREDENTIALS;
import static com.tcp.cart.constants.CartTestConstants.CREDENTIAL_TYPE_GUEST;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import com.tcp.cart.application.CartApplication;
import com.tcp.cart.config.CartProperties;
import com.tcp.cart.constants.CartConstants;
import com.tcp.framework.response.APIResponse;
import com.tcp.framework.response.ResponseStatus;
import com.tcp.framework.sfcc.test.SFCCBaseTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = CartApplication.class)
@Profile("default & test")
class TokenAPITest extends SFCCBaseTest {

	@Autowired
	private CartProperties properties;

	@Test
	void testGetTokenWithCredentials() {

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(CartConstants.CREDENTIAL_TYPE, CREDENTIAL_TYPE_CREDENTIALS);

		EntityExchangeResult<APIResponse> returnResult = webClient.post().uri(API_ENDPOINT_TOKEN)
				.contentType(MediaType.APPLICATION_JSON).header(API_HEADER_USERNAME, properties.getUserName())
				.header(API_HEADER_PASSWORD, properties.getPassword()).bodyValue(requestBody).exchange()
				.expectBody(APIResponse.class).returnResult();

		Assertions.assertNotNull(returnResult.getResponseBody());
		Assertions.assertNotNull(returnResult.getRequestHeaders());
		Assertions.assertEquals(HttpStatus.OK, returnResult.getStatus());
		Assertions.assertEquals(returnResult.getResponseBody().getStatus().name(), ResponseStatus.SUCCESS.name());
		APIResponse response = returnResult.getResponseBody();
		Object responseData = response.getData();
		Assertions.assertNotNull(responseData);

	}

	@Test
	void testGetTokenWithGuest() {

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(CartConstants.CREDENTIAL_TYPE, CREDENTIAL_TYPE_GUEST);

		EntityExchangeResult<APIResponse> returnResult = webClient.post().uri(API_ENDPOINT_TOKEN)
				.contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange().expectBody(APIResponse.class)
				.returnResult();

		Assertions.assertNotNull(returnResult.getResponseBody());
		Assertions.assertNotNull(returnResult.getRequestHeaders());
		Assertions.assertEquals(HttpStatus.OK, returnResult.getStatus());
		Assertions.assertEquals(returnResult.getResponseBody().getStatus().name(), ResponseStatus.SUCCESS.name());
		APIResponse response = returnResult.getResponseBody();
		Object responseData = response.getData();
		Assertions.assertNotNull(responseData);

	}

	@Test
	void testGetTokenWithInvalidCredentials() {

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(CartConstants.CREDENTIAL_TYPE, CREDENTIAL_TYPE_CREDENTIALS);

		EntityExchangeResult<APIResponse> returnResult = webClient.post().uri(API_ENDPOINT_TOKEN)
				.contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange().expectBody(APIResponse.class)
				.returnResult();
		Assertions.assertNotNull(returnResult.getResponseBody());
		Assertions.assertNotNull(returnResult.getRequestHeaders());
		Assertions.assertEquals(HttpStatus.OK, returnResult.getStatus());
		Assertions.assertEquals(returnResult.getResponseBody().getStatus().name(), ResponseStatus.SUCCESS.name());
		APIResponse response = returnResult.getResponseBody();
		Object responseData = response.getErrors();
		Assertions.assertNull(responseData);

	}

}