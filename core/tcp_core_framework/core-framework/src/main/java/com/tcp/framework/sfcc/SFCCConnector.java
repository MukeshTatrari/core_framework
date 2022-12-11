package com.tcp.framework.sfcc;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcp.framework.annotations.PerformanceLogger;

import lombok.RequiredArgsConstructor;

/**
 * Used to call SFCC external APIS , using rest template.
 */
@Service
@RequiredArgsConstructor
public class SFCCConnector {

	private final RestTemplate template;

	/**
	 * 
	 * @param <T>
	 * @param uri
	 * @param method
	 * @param entity
	 * @return
	 * 
	 *         calling external SFCC API using provided url ,HTTP method and
	 *         HttpEntity
	 */

	@PerformanceLogger
	public <T> ResponseEntity<T> getResponse(String uri, HttpMethod method, HttpEntity<?> entity,
			Class<T> responseType) {

		return template.exchange(uri, method, entity, responseType);

	}

}
