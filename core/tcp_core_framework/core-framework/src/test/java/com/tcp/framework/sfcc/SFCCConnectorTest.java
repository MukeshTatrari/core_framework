package com.tcp.framework.sfcc;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
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
import org.springframework.web.client.RestTemplate;

public class SFCCConnectorTest {

	@InjectMocks
	private SFCCConnector connector;

	@Mock
	private RestTemplate template;

	@Before
	public void init() throws IOException {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getExternalAPI() {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		responseHeaders.add(SFCCConstants.AUTHORIZATION, SFCCConstants.BEARER_TOKEN);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth("asdjkashdhas");

		HttpEntity<Map> requestEntity = new HttpEntity<Map>(null, headers);
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("response", "ok");

		ResponseEntity<Map> response = new ResponseEntity<Map>(responseMap, responseHeaders, HttpStatus.OK);

		Mockito.when(template.exchange(SFCCConstants.BASE_URL, HttpMethod.GET, requestEntity, Map.class))
				.thenReturn(response);

		ResponseEntity<?> data = connector.getResponse(SFCCConstants.BASE_URL, HttpMethod.GET, requestEntity,
				Map.class);

		HttpStatus responseStatus = response.getStatusCode();

		assertThat(responseStatus.compareTo(HttpStatus.OK));
	}
}
