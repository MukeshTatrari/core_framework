package com.tcp.framework.exception.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.tcp.framework.exception.TCPRuntimeException;

public class TCPRuntimeExceptionTest {

	private static final String EXCEPTION = "Exception Has been Occurred";

	@Test
	public void TestRuntimeExceptionWithMessage() {
		TCPRuntimeException ex = new TCPRuntimeException(EXCEPTION);
		assertEquals(EXCEPTION,ex.getMessage());
	}

	@Test
	public void TestRuntimeException() {
		TCPRuntimeException ex = new TCPRuntimeException(EXCEPTION,
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		assertEquals(EXCEPTION,ex.getMessage());
	}

}
