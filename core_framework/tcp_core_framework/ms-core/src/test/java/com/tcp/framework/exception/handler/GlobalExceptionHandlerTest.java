package com.tcp.framework.exception.handler;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.ServletWebRequest;

import com.tcp.framework.exception.TCPRuntimeException;
import com.tcp.framework.response.APIResponse;

class GlobalExceptionHandlerTest {

	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testHandleMissingServletRequestParameter() {
		MissingServletRequestParameterException ex = new MissingServletRequestParameterException("Parameter Name",
				"Parameter Type");
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<Object> actualHandleMissingServletRequestParameterResult = this.globalExceptionHandler
				.handleMissingServletRequestParameter(ex, headers, HttpStatus.CONTINUE,
						new ServletWebRequest(new MockHttpServletRequest()));
		assertEquals(HttpStatus.BAD_REQUEST, actualHandleMissingServletRequestParameterResult.getStatusCode());
		assertTrue(actualHandleMissingServletRequestParameterResult.hasBody());
		assertNotNull(
				((APIResponse) actualHandleMissingServletRequestParameterResult.getBody()).getError().getErrorCode());
	}

	@Test
	void testHandleConstraintViolation() {
		ConstraintViolationException ex = new ConstraintViolationException(new HashSet<ConstraintViolation<?>>());
		ResponseEntity<Object> actualHandleConstraintViolationResult = this.globalExceptionHandler
				.handleConstraintViolation(ex, new ServletWebRequest(new MockHttpServletRequest()));
		assertEquals(HttpStatus.BAD_REQUEST, actualHandleConstraintViolationResult.getStatusCode());
		assertTrue(actualHandleConstraintViolationResult.hasBody());
		assertNotNull(((APIResponse) actualHandleConstraintViolationResult.getBody()).getError().getErrorCode());
	}

	@Test
	void testHandleConstraintViolation2() {
		ConstraintViolationException ex = new ConstraintViolationException("An error occurred", new HashSet<>());
		ResponseEntity<Object> actualHandleConstraintViolationResult = this.globalExceptionHandler
				.handleConstraintViolation(ex, new ServletWebRequest(new MockHttpServletRequest()));
		assertEquals(HttpStatus.BAD_REQUEST, actualHandleConstraintViolationResult.getStatusCode());
		assertTrue(actualHandleConstraintViolationResult.hasBody());
		assertNotNull(((APIResponse) actualHandleConstraintViolationResult.getBody()).getError().getErrorCode());
	}

	@Test
	void testHandleConstraintViolation3() {
		ConstraintViolationException constraintViolationException = new ConstraintViolationException(
				"An error occurred", new HashSet<ConstraintViolation<?>>());
		constraintViolationException.addSuppressed(new Throwable());
		ResponseEntity<Object> actualHandleConstraintViolationResult = this.globalExceptionHandler
				.handleConstraintViolation(constraintViolationException,
						new ServletWebRequest(new MockHttpServletRequest()));
		assertEquals(HttpStatus.BAD_REQUEST, actualHandleConstraintViolationResult.getStatusCode());
		assertTrue(actualHandleConstraintViolationResult.hasBody());
		assertNotNull(((APIResponse) actualHandleConstraintViolationResult.getBody()).getError().getErrorCode());
	}

	@Test
	void testHandleHttpRequestMethodNotSupported() {
		HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException(
				"http://localhost:9001/cart-service/v1/carts/123");
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<Object> actualHandleHttpRequestMethodNotSupportedResult = this.globalExceptionHandler
				.handleHttpRequestMethodNotSupported(ex, headers, HttpStatus.CONTINUE,
						new ServletWebRequest(new MockHttpServletRequest()));
		assertEquals(HttpStatus.BAD_REQUEST, actualHandleHttpRequestMethodNotSupportedResult.getStatusCode());
		assertTrue(actualHandleHttpRequestMethodNotSupportedResult.hasBody());
		assertNotNull(
				((APIResponse) actualHandleHttpRequestMethodNotSupportedResult.getBody()).getError().getErrorCode());
	}

	@Test
	void testHandleHttpMediaTypeNotSupported() {
		HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException("An error occurred");
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<Object> actualHandleHttpMediaTypeNotSupportedResult = this.globalExceptionHandler
				.handleHttpMediaTypeNotSupported(ex, headers, HttpStatus.CONTINUE,
						new ServletWebRequest(new MockHttpServletRequest()));
		assertEquals(HttpStatus.BAD_REQUEST, actualHandleHttpMediaTypeNotSupportedResult.getStatusCode());
		assertTrue(actualHandleHttpMediaTypeNotSupportedResult.hasBody());
		assertNotNull(((APIResponse) actualHandleHttpMediaTypeNotSupportedResult.getBody()).getError().getErrorCode());
	}

	@Test
	void testHandleAll() {
		Exception ex = new Exception();
		ResponseEntity<Object> actualHandleAllResult = this.globalExceptionHandler.handleAll(ex,
				new ServletWebRequest(new MockHttpServletRequest()));
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualHandleAllResult.getStatusCode());
		assertTrue(actualHandleAllResult.hasBody());
	}

	@Test
	void testHandleHttpClientErrorException() {
		HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.NOT_FOUND);
		ResponseEntity<Object> actualHandleHttpClientErrorExceptionResult = this.globalExceptionHandler
				.handleHttpClientErrorException(ex, new ServletWebRequest(new MockHttpServletRequest()));
		assertEquals(HttpStatus.BAD_REQUEST, actualHandleHttpClientErrorExceptionResult.getStatusCode());
		assertTrue(actualHandleHttpClientErrorExceptionResult.hasBody());
		assertNotNull(((APIResponse) actualHandleHttpClientErrorExceptionResult.getBody()).getError().getErrorCode());
	}

	@Test
	void testHandleHttpServerErrorException() {
		HttpServerErrorException ex = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
		ResponseEntity<Object> actualHandleHttpServerErrorExceptionResult = this.globalExceptionHandler
				.handleHttpServerErrorException(ex, new ServletWebRequest(new MockHttpServletRequest()));
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualHandleHttpServerErrorExceptionResult.getStatusCode());
		assertTrue(actualHandleHttpServerErrorExceptionResult.hasBody());
		assertNotNull(((APIResponse) actualHandleHttpServerErrorExceptionResult.getBody()).getError().getErrorCode());
	}

	@Test
	void testHandleTCPRuntimeException() {
		TCPRuntimeException ex = new TCPRuntimeException("Base Exception", 400);
		ResponseEntity<Object> actualHandleTCPRuntimeExceptionResult = this.globalExceptionHandler
				.handleTCPRuntimeException(ex, new ServletWebRequest(new MockHttpServletRequest()));
		assertEquals(HttpStatus.BAD_REQUEST, actualHandleTCPRuntimeExceptionResult.getStatusCode());
		assertTrue(actualHandleTCPRuntimeExceptionResult.hasBody());
		assertNotNull(((APIResponse) actualHandleTCPRuntimeExceptionResult.getBody()).getError().getErrorCode());
	}
}