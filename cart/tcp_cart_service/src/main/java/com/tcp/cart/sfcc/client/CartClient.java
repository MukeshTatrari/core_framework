package com.tcp.cart.sfcc.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tcp.cart.config.CartProperties;
import com.tcp.cart.constants.CartConstants;
import com.tcp.cart.dto.BasketItemsDTO;
import com.tcp.cart.dto.CartResponseDTO;
import com.tcp.framework.annotations.PerformanceLogger;
import com.tcp.framework.sfcc.client.SFCCRestClient;
import com.tcp.framework.util.RestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * used for connecting the SFCC cart API
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CartClient {

	private final SFCCRestClient sfccRestClient;

	private final CartProperties cartProperties;

	/**
	 * 
	 * @param basketId
	 * @return CartResponseDTO
	 * 
	 *         Fetch basket information based on the basketId
	 */

	public CartResponseDTO getCart(String basketId) {

		String url = cartProperties.getBaseUrl() + cartProperties.getGetCartUri();
		String basketUrl = url.replace(CartConstants.PARAM_BASET_ID, basketId);
		log.debug("basketUrl :::: ", basketUrl);
		ResponseEntity<?> response = sfccRestClient.getResponseWithToken(basketUrl, HttpMethod.GET, new HttpEntity<>(RestUtil.buildCommonHeader()),
				CartResponseDTO.class);
		return (CartResponseDTO) response.getBody();

	}

	/**
	 * 
	 * @return
	 * 
	 *         call create basket api for customer based on customer bearer token.
	 *         and return the basket information in response
	 */

	@PerformanceLogger(value = "Creating Empty Basket")
	public CartResponseDTO createCart() {

		String createBasketUrl = cartProperties.getBaseUrl() + cartProperties.getCreateCartUri();
		ResponseEntity<?> response = sfccRestClient.getResponseWithToken(createBasketUrl, HttpMethod.POST, "{}",
				CartResponseDTO.class);
		log.info("Cart Basker response :::: ", response.getBody());
		return (CartResponseDTO) response.getBody();
	}

	/**
	 *
	 * @param basketId
	 * @param items
	 * @return
	 *
	 * adding the items in the basket
	 */
	public CartResponseDTO addItemsToBasket(String basketId, BasketItemsDTO items) {

		String url = cartProperties.getBaseUrl() + cartProperties.getAddItemsUri();
		String addItemUrl = url.replace(CartConstants.PARAM_BASET_ID, basketId);
		log.debug("addItemUrl :::: ", addItemUrl);
		ResponseEntity<?> response = sfccRestClient.getResponseWithToken(addItemUrl,
						HttpMethod.POST, new HttpEntity<>(items.getItemList()),CartResponseDTO.class);
		return (CartResponseDTO) response.getBody();

	}
}
