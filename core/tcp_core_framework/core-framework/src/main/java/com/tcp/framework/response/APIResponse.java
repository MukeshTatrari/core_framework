package com.tcp.framework.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 
 * @param <T>
 * 
 * Base Response entity for custom responses
 */
@Data
@JsonInclude(Include.NON_EMPTY)
public class APIResponse {

	private ResponseStatus status;
	private LocalDateTime timestamp;
	private Object data;
	private ErrorResponse error;

	/**
	 * 
	 * @param data
	 * @param status
	 */
	public APIResponse(Object data, ResponseStatus status) {
		super();
		this.status = status;
		this.timestamp = LocalDateTime.now();
		this.data = data;
	}

	/**
	 * 
	 * @param data
	 * @param status
	 */
	public APIResponse(String errorCode, String errorMessage, ResponseStatus status) {
		super();
		this.status = status;
		this.timestamp = LocalDateTime.now();
		this.error = new ErrorResponse(errorCode, errorMessage);
	}
}
