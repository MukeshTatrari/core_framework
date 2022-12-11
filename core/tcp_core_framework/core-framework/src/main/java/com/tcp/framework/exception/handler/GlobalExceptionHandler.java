package com.tcp.framework.exception.handler;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tcp.framework.exception.TCPRuntimeException;
import com.tcp.framework.response.APIResponse;
import com.tcp.framework.response.ResponseStatus;

/**
 * Global Exception handler handles all kind of exceptions using exception
 * handler methods so that error messages going out of controller can be handled
 * in consistent way
 */
@Component
@ControllerAdvice(basePackages = { "com.tcp" })
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		APIResponse apiError = new APIResponse(ex.getMessage(), ex.getLocalizedMessage(),ResponseStatus.ERROR);
		LOG.error("MethodArgumentNotValidException occured {}", errors);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		LOG.error(ex.getLocalizedMessage());
		APIResponse apiError = new APIResponse(ex.getMessage(), ex.getLocalizedMessage(),ResponseStatus.ERROR);
		LOG.error("MissingServletRequestParameterException occured {}", error);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
					+ violation.getMessage());
		}

		APIResponse apiError = new APIResponse(ex.getMessage(), ex.getLocalizedMessage(),ResponseStatus.ERROR);
		LOG.error("ConstraintViolationException occured {}", errors);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

		APIResponse apiError = new APIResponse(ex.getMessage(), ex.getLocalizedMessage(),ResponseStatus.ERROR);
		LOG.error("MethodArgumentTypeMismatchException occured {}", error);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder error = new StringBuilder();
		error.append(ex.getMethod());
		error.append(" method is not supported for this request.");

		APIResponse apiError = new APIResponse(ex.getMessage(), ex.getLocalizedMessage(),ResponseStatus.ERROR);
		LOG.error("HttpRequestMethodNotSupportedException occured {}", error);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder error = new StringBuilder();
		error.append(ex.getContentType());
		error.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> error.append(t + ", "));

		APIResponse apiError = new APIResponse(ex.getMessage(), ex.getLocalizedMessage(),ResponseStatus.ERROR);
		LOG.error("HttpMediaTypeNotSupportedException occured {}", error);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		APIResponse apiError = new APIResponse(ex.getMessage(), ex.getLocalizedMessage(),ResponseStatus.ERROR);
		LOG.error("Exception occured {} ", ex.getLocalizedMessage());
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ HttpClientErrorException.class })
	public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
		APIResponse apiError = new APIResponse(ex.getMessage(), ex.getLocalizedMessage(),ResponseStatus.ERROR);
		LOG.error("HttpClientErrorException occured {} ", ex.getLocalizedMessage());
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ HttpServerErrorException.class })
	public ResponseEntity<Object> handleHttpServerErrorException(HttpServerErrorException ex, WebRequest request) {
		APIResponse apiError = new APIResponse(ex.getMessage(), ex.getLocalizedMessage(),ResponseStatus.ERROR);
		LOG.error("HttpServerErrorException occured {} ", ex.getLocalizedMessage());
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ TCPRuntimeException.class })
	public ResponseEntity<Object> handleTCPRuntimeException(TCPRuntimeException ex, WebRequest request) {
		APIResponse apiError = new APIResponse(ex.getMessage(), ex.getLocalizedMessage(),ResponseStatus.ERROR);
		LOG.error("BaseException occured {} ", ex.getLocalizedMessage());
		return new ResponseEntity<>(apiError, HttpStatus.valueOf(ex.getErrorCode()));
	}
}
