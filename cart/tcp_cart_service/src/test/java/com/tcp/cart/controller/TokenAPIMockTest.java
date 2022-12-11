package com.tcp.cart.controller;

import static com.tcp.cart.constants.CartTestConstants.API_ENDPOINT_TOKEN;
import static com.tcp.cart.constants.CartTestConstants.API_HEADER_PASSWORD;
import static com.tcp.cart.constants.CartTestConstants.API_HEADER_USERNAME;
import static com.tcp.cart.constants.CartTestConstants.AUTHORIZATION;
import static com.tcp.cart.constants.CartTestConstants.BEARER_TOKEN;
import static com.tcp.cart.constants.CartTestConstants.CREDENTIAL_TYPE_CREDENTIALS;
import static com.tcp.cart.constants.CartTestConstants.CREDENTIAL_TYPE_GUEST;
import static com.tcp.cart.constants.CartTestConstants.TOKEN_URL;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import com.tcp.cart.application.CartApplication;
import com.tcp.cart.config.CartProperties;
import com.tcp.cart.constants.CartConstants;
import com.tcp.framework.response.APIResponse;
import com.tcp.framework.response.ResponseStatus;
import com.tcp.framework.sfcc.client.SFCCRestClient;
import com.tcp.framework.sfcc.client.SFCCTokenClient;
import com.tcp.framework.sfcc.test.SFCCBaseTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = CartApplication.class)
@Profile("default & test")
@Slf4j
class TokenAPIMockTest extends SFCCBaseTest {

	@Mock
	SFCCTokenClient sfccTokenClient;

	@Mock
	SFCCRestClient restClient;

	@Autowired
	private CartProperties cartProperties;

	@BeforeEach
	public void init() throws IOException {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetTokenWithGuest() {

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(CartConstants.CREDENTIAL_TYPE, CREDENTIAL_TYPE_GUEST);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		responseHeaders.add(AUTHORIZATION, BEARER_TOKEN);

		ResponseEntity<Map> response = new ResponseEntity<Map>(responseHeaders, HttpStatus.OK);

		log.info("Test:: testCreateBasketWithGuest :: started");
		Mockito.when(
				restClient.getResponse(TOKEN_URL, HttpMethod.POST, new HttpEntity<>(requestBody, headers), Map.class))
				.thenReturn(response);

		EntityExchangeResult<APIResponse> returnResult = webClient.post().uri(API_ENDPOINT_TOKEN)
				.contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange().expectBody(APIResponse.class)
				.returnResult();

		Assertions.assertNotNull(returnResult.getResponseBody());
		Assertions.assertNotNull(returnResult.getRequestHeaders());
		Assertions.assertEquals(HttpStatus.OK, returnResult.getStatus());
		Assertions.assertEquals(returnResult.getResponseBody().getStatus().name(), ResponseStatus.SUCCESS.name());

	}

	@Test
	void testGetTokenWithInvalidCredentials() {

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put(CartConstants.CREDENTIAL_TYPE, CREDENTIAL_TYPE_CREDENTIALS);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth("Mukesh", "abc@123");

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		responseHeaders.add(AUTHORIZATION, BEARER_TOKEN);

		ResponseEntity<Map> response = new ResponseEntity<Map>(responseHeaders, HttpStatus.OK);

		log.info("Test:: testCreateBasketWithGuest :: started");
		Mockito.when(
				restClient.getResponse(TOKEN_URL, HttpMethod.POST, new HttpEntity<>(requestBody, headers), Map.class))
				.thenReturn(response);

		EntityExchangeResult<APIResponse> returnResult = webClient.post().uri(API_ENDPOINT_TOKEN)
				.contentType(MediaType.APPLICATION_JSON).header(API_HEADER_USERNAME, cartProperties.getUserName())
				.header(API_HEADER_PASSWORD, cartProperties.getPassword()).bodyValue(requestBody).exchange()
				.expectBody(APIResponse.class).returnResult();

		Assertions.assertNotNull(returnResult.getResponseBody());
		Assertions.assertNotNull(returnResult.getRequestHeaders());
		Assertions.assertEquals(HttpStatus.OK, returnResult.getStatus());
		Assertions.assertEquals(returnResult.getResponseBody().getStatus().name(), ResponseStatus.SUCCESS.name());

	}

}
