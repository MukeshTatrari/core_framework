package com.tcp.framework.sfcc.client;

import static com.tcp.framework.sfcc.constant.SFCCConstants.CREDENTIAL_TYPE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tcp.framework.annotations.PerformanceLogger;
import com.tcp.framework.sfcc.config.SFCCConfig;

import lombok.RequiredArgsConstructor;

/**
 * 
 * used for connecting the SFCC cart API
 *
 */
@Service
@RequiredArgsConstructor
public class SFCCClient {

	private final SFCCRestClient restClient;

	private final SFCCConfig config;

	/**
	 * 
	 * @return
	 * 
	 *         Call the token service for the customer with its userName and
	 *         password to get the bearer token.
	 * 
	 */
	public String getToken(String userName, String password, String bodyType) {

		String loginUrl = config.getTokenUrl();

		// set basic authentication with userName and password
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(userName, password);

		Map<String, String> body = new HashMap<>();
		body.put(CREDENTIAL_TYPE, bodyType);

		HttpEntity<Object> entity = new HttpEntity<>(body, headers);

		ResponseEntity<?> response = restClient.getResponse(loginUrl, HttpMethod.POST, entity, Map.class);

		HttpHeaders responseHeaders = response.getHeaders();
		return responseHeaders.getFirst("Authorization");
	}

	/**
	 * 
	 * @return
	 * 
	 *         Call the token service for the GuestUser
	 * 
	 */
	@PerformanceLogger(value = "Getting Guest Token")
	public String getGuestToken(String bodyType) {

		String loginUrl = config.getTokenUrl();

		// set basic authentication with userName and password
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> body = new HashMap<>();
		body.put(CREDENTIAL_TYPE, bodyType);

		HttpEntity<Object> entity = new HttpEntity<>(body, headers);

		ResponseEntity<?> response = restClient.getResponse(loginUrl, HttpMethod.POST, entity, Map.class);

		HttpHeaders responseHeaders = response.getHeaders();
		return responseHeaders.getFirst("Authorization");

	}
}
