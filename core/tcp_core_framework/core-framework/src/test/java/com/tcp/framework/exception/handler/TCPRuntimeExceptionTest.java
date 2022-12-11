package com.tcp.framework.exception.handler;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.tcp.framework.exception.TCPRuntimeException;

public class TCPRuntimeExceptionTest {

	@Test
	public void TestRuntimeExceptionWithMessage() {
		TCPRuntimeException ex = new TCPRuntimeException("Exception Has been Occurred");
	}

	@Test
	public void TestRuntimeException() {
		TCPRuntimeException ex = new TCPRuntimeException("Exception Has been Occurred",
				HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

}
