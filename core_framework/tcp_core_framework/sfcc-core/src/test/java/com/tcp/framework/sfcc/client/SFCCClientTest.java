package com.tcp.framework.sfcc.client;

import static com.tcp.framework.sfcc.constant.SFCCTestConstants.AUTHENTICATION_TYPE;
import static com.tcp.framework.sfcc.constant.SFCCTestConstants.AUTHORIZATION;
import static com.tcp.framework.sfcc.constant.SFCCTestConstants.BASE_URL;
import static com.tcp.framework.sfcc.constant.SFCCTestConstants.BEARER_TOKEN;
import static com.tcp.framework.sfcc.constant.SFCCTestConstants.LOGIN_URL;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.tcp.framework.sfcc.config.SFCCConfig;

public class SFCCClientTest {

	@Mock
	public SFCCConfig config;

	@InjectMocks
	public SFCCClient sfccClient;

	@Mock
	public SFCCRestClient restClient;

	@Before
	public void init() throws IOException {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getGuestToken() {

		Map<String, String> body = new HashMap<>();
		body.put(AUTHENTICATION_TYPE, BEARER_TOKEN);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		responseHeaders.add(AUTHORIZATION, BEARER_TOKEN);

		HttpEntity<Map> entity = new HttpEntity<Map>(body, headers);
		ResponseEntity<Map> loginResponse = new ResponseEntity<Map>(responseHeaders, HttpStatus.OK);
		Mockito.when(config.getTokenUrl()).thenReturn(BASE_URL + LOGIN_URL);

		Mockito.when(restClient.getResponse(BASE_URL + LOGIN_URL, HttpMethod.POST, entity, Map.class))
				.thenReturn(loginResponse);

		String token = sfccClient.getGuestToken("Bearer asdjkashdhas");
		assertEquals(BEARER_TOKEN, token);

	}

	@Test
	public void getToken() {

		Map<String, String> body = new HashMap<>();
		body.put(AUTHENTICATION_TYPE, "credentials");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth("mukesh", "mukesh@123");

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		responseHeaders.add(AUTHORIZATION, BEARER_TOKEN);

		HttpEntity<Map> entity = new HttpEntity<Map>(body, headers);
		ResponseEntity<Map> loginResponse = new ResponseEntity<Map>(responseHeaders, HttpStatus.OK);
		Mockito.when(config.getTokenUrl()).thenReturn(BASE_URL + LOGIN_URL);

		Mockito.when(restClient.getResponse(BASE_URL + LOGIN_URL, HttpMethod.POST, entity, Map.class))
				.thenReturn(loginResponse);

		String token = sfccClient.getToken("mukesh", "mukesh@123", "credentials");

		assertEquals(BEARER_TOKEN, token);

	}
}