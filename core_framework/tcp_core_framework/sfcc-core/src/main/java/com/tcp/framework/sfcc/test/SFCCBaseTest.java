package com.tcp.framework.sfcc.test;

import static com.tcp.framework.sfcc.constant.SFCCConstants.CREDENTIAL_TYPE_CREDENTIALS;
import static com.tcp.framework.sfcc.constant.SFCCConstants.CREDENTIAL_TYPE_GUEST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;

import com.tcp.framework.sfcc.client.SFCCClient;
import com.tcp.framework.sfcc.config.SFCCConfig;

public class SFCCBaseTest {

	@Autowired
	public WebTestClient webClient;

	@Autowired
	private SFCCClient sfccClient;

	@Autowired
	private SFCCConfig config;

	private String guestUserToken;
	private String loggedInUserToken;

	/**
	 * Method returns Guest User Token
	 * 
	 * @return
	 */
	public String getGuestUserToken() {
		if (StringUtils.isEmpty(guestUserToken)) {
			guestUserToken = generateGuestToken();
		}
		return guestUserToken;
	}

	/**
	 * Method returns Logged In User Token
	 * 
	 * @return
	 */
	public String getLoggedInUserToken() {
		if (StringUtils.isEmpty(loggedInUserToken)) {
			loggedInUserToken = generateLoggedInUserToken();
		}
		return loggedInUserToken;
	}

	/**
	 * This method generates Guest Token
	 */
	public String generateGuestToken() {
		return sfccClient.getGuestToken(CREDENTIAL_TYPE_GUEST);
	}

	/**
	 * This method generates Guest Token
	 */
	public String generateLoggedInUserToken() {
		return sfccClient.getToken(config.getUserName(), config.getPassword(), CREDENTIAL_TYPE_CREDENTIALS);
	}
}