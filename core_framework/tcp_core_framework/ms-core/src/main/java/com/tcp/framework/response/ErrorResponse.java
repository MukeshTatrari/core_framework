package com.tcp.framework.response;

import lombok.Data;

/**
 * 
 * error response class
 *
 */
@Data
public class ErrorResponse {

	private String errorCode;
	private String errorMessage;

	/**
	 * 
	 * @param errorCode
	 * @param message
	 */
	public ErrorResponse(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
