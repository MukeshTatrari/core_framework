package com.tcp.cart.util;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import com.tcp.framework.response.APIResponse;
import com.tcp.framework.response.ResponseStatus;

public class AssertionUtils {

	/**
	 * Performs common validations and returns response data
	 * @param returnResult
	 * @param httpStatus
	 * @return
	 */
	public static Object assertSuccessResponse(EntityExchangeResult<APIResponse> returnResult, HttpStatus httpStatus) {
		Assertions.assertNotNull(returnResult.getResponseBody());
		Assertions.assertEquals(httpStatus, returnResult.getStatus());
		Assertions.assertEquals(returnResult.getResponseBody().getStatus().name(), ResponseStatus.SUCCESS.name());
		Object data = returnResult.getResponseBody().getData();
		Assertions.assertNotNull(data);
		return data;
	}

	
	/**
	 * Performs common validations and returns error data
	 * @param returnResult
	 * @param httpStatus
	 * @return
	 */
	public static Object assertErrorResponse(EntityExchangeResult<APIResponse> returnResult, HttpStatus httpStatus) {
		Assertions.assertNotNull(returnResult.getResponseBody());
		Assertions.assertEquals(httpStatus, returnResult.getStatus());
		Assertions.assertEquals(returnResult.getResponseBody().getStatus().name(), ResponseStatus.ERROR.name());
		Object data = returnResult.getResponseBody().getErrors();
		Assertions.assertNotNull(data);
		return data;
	}

}
