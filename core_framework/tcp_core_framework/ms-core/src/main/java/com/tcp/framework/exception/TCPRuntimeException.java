package com.tcp.framework.exception;

import lombok.Getter;

/**
 * Exception handler For handling the runtime exceptions , we have provided 
 * a way through which we can provide a customize errorcode and messages.
 * we can extend this class and can throw custom exception from microservices.
 */
@Getter
public class TCPRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int errorCode;

	/**
	 * @param message
	 * @param code
	 * 
	 * Use error code and error messages for custom exceptions.
	 */
	public TCPRuntimeException(String message, int code) {
		super(message);
		this.errorCode = code;
	}


	/**
	 * @param message
	 * 
	 * Error message for custom exceptions.
	 */
	public TCPRuntimeException(String message) {
		super(message);
	}

}
