package com.tcp.cart.controller;

import com.tcp.cart.dto.TokenDTO;
import com.tcp.framework.response.APIResponse;
import com.tcp.framework.response.ResponseStatus;
import com.tcp.framework.sfcc.client.SFCCTokenClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * Controller to get the Guest and authentication token for customer.
 *
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {

	private final SFCCTokenClient sfccTokenClient;

	/**
	 *
	 * @param userName
	 * @param password
	 * @return
	 * 
	 *         generate the user Authentication and Guest token
	 */
	@PostMapping("/token")
	public ResponseEntity<APIResponse> getToken(
			@RequestHeader(required = false) String userName, @RequestHeader(required = false) String password) {

		log.debug("-->getToken()");

		if (isGuestUser(userName, password)) {
			return new ResponseEntity<>(
					new APIResponse(new TokenDTO(sfccTokenClient.getGuestToken()),
							ResponseStatus.SUCCESS),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new APIResponse(
					new TokenDTO(sfccTokenClient.getUserToken(userName,password)),
					ResponseStatus.SUCCESS), HttpStatus.OK);
		}

	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * 
	 *         check if user is guest user
	 */
	private boolean isGuestUser(String userName, String password) {
		return (ObjectUtils.isEmpty(password) || ObjectUtils.isEmpty(userName));
	}

}
