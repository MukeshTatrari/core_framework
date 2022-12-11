package com.tcp.cart.controller;

import static com.tcp.cart.constants.CartTestConstants.API_ENDPOINT_ADD_ITEMS_NEW_BASKET;
import static com.tcp.cart.constants.CartTestConstants.API_ENDPOINT_ADD_ITEMS_WITH_BASKET_ID;
import static com.tcp.cart.constants.CartTestConstants.API_ENDPOINT_CREATE_CART;
import static com.tcp.cart.constants.CartTestConstants.API_ENDPOINT_GET_BASKET;
import static com.tcp.cart.constants.CartTestConstants.API_HEADER_AUTHORIZATION;
import static com.tcp.cart.constants.CartTestConstants.PRODUCT_ID;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import com.tcp.cart.application.CartApplication;
import com.tcp.cart.config.CartProperties;
import com.tcp.cart.constants.CartConstants;
import com.tcp.cart.dto.BasketItemsDTO;
import com.tcp.cart.dto.CartResponseDTO;
import com.tcp.cart.dto.ProductDTO;
import com.tcp.cart.util.AssertionUtils;
import com.tcp.cart.util.CartTestUtil;
import com.tcp.framework.response.APIResponse;
import com.tcp.framework.response.ResponseStatus;
import com.tcp.framework.sfcc.client.SFCCRestClient;
import com.tcp.framework.sfcc.client.impl.SFCCTokenClientImpl;
import com.tcp.framework.sfcc.test.SFCCBaseTest;
import com.tcp.framework.util.RestUtil;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = CartApplication.class)
@Profile("default & test")
@Slf4j
class CartAPIMockTest extends SFCCBaseTest {

	private static final String BASKET_ID = "1234";

	@MockBean
	SFCCRestClient sfccRestClient;

	@MockBean
	SFCCTokenClientImpl sfccClient;

	@Autowired
	private CartProperties cartProperties;

	@BeforeEach
	public void init() throws IOException {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testCreateBaskeAndAddItemtWithGuest() {

		String createBasketUrl = cartProperties.getBaseUrl() + cartProperties.getCreateCartUri();
		log.info("Test:: testCreateBasketWithGuest :: started");
		Mockito.when(sfccClient.getGuestToken()).thenReturn("token");
		Mockito.when(sfccRestClient.getResponseWithToken(createBasketUrl, HttpMethod.POST, "{}", CartResponseDTO.class))
				.thenReturn(new ResponseEntity<>(CartTestUtil.getCartResponseDTO(), HttpStatus.CREATED));

		log.info("Test:: testCreateBasketWithGuest :: started");

		// create basket
		EntityExchangeResult<APIResponse> returnResult = webClient.post().uri(API_ENDPOINT_CREATE_CART)
				.contentType(MediaType.APPLICATION_JSON).header(API_HEADER_AUTHORIZATION, getGuestUserToken())
				.exchange().expectStatus().is2xxSuccessful().expectBody(APIResponse.class).returnResult();

		Assertions.assertNotNull(returnResult.getResponseBody());
		Assertions.assertNotNull(returnResult.getRequestHeaders());
		Assertions.assertNotNull(returnResult.getResponseBody().getData());
		Assertions.assertEquals(HttpStatus.CREATED, returnResult.getStatus());
		Assertions.assertEquals(returnResult.getResponseBody().getStatus().name(), ResponseStatus.SUCCESS.name());
		APIResponse response = returnResult.getResponseBody();
		LinkedHashMap<String, Object> responseData = (LinkedHashMap<String, Object>) response.getData();
		String basketId = (String) responseData.get("basketId");
		Assertions.assertNotNull(responseData);
		Assertions.assertNotNull(basketId);

	}

	@Test
	void testGetBaskeWithGuest() {

		log.info("Test:: testCreateBasketWithGuest :: started");

		String getUri = cartProperties.getBaseUrl() + cartProperties.getGetCartUri();
		String basketUrl = getUri.replace(CartConstants.PARAM_BASET_ID, BASKET_ID);
		log.info("Test:: testCreateBasketWithGuest :: started");
		Mockito.when(sfccClient.getGuestToken()).thenReturn("token");
		Mockito.when(sfccRestClient.getResponseWithToken(basketUrl, HttpMethod.GET,
				new HttpEntity<>(RestUtil.buildCommonHeader()), CartResponseDTO.class))
				.thenReturn(new ResponseEntity<>(CartTestUtil.getCartResponseDTO(), HttpStatus.OK));

		log.info("Test:: testCreateBasketWithGuest :: started");

		// create basket
		EntityExchangeResult<APIResponse> returnResult = webClient.get()
				.uri(API_ENDPOINT_GET_BASKET.replace("{basketId}", BASKET_ID))
				.header(API_HEADER_AUTHORIZATION, getGuestUserToken()).exchange().expectStatus().is2xxSuccessful()
				.expectBody(APIResponse.class).returnResult();

		Object data = AssertionUtils.assertSuccessResponse(returnResult, HttpStatus.OK);

		LinkedHashMap<String, Object> responseData = (LinkedHashMap<String, Object>) data;
		String basketId = (String) responseData.get("basketId");
		Assertions.assertNotNull(responseData);
		Assertions.assertNotNull(basketId);
	}

	@Test
	void testAddItemsToBasketWithGuestUser() {

		log.info("Test:: testAddItemsToBasketWithGuestUserValidProduct :: started");

		BasketItemsDTO basket = createAddItemRequest(PRODUCT_ID);
		String url = cartProperties.getBaseUrl() + cartProperties.getAddItemsUri();
		String addItemUrl = url.replace(CartConstants.PARAM_BASET_ID, BASKET_ID);

		log.info("Test:: testCreateBasketWithGuest :: started");
		Mockito.when(sfccClient.getGuestToken()).thenReturn("token");
		Mockito.when(sfccRestClient.getResponseWithToken(addItemUrl, HttpMethod.POST,
				new HttpEntity<>(basket.getItemList()), CartResponseDTO.class))
				.thenReturn(new ResponseEntity<>(CartTestUtil.getCartResponseDTO(), HttpStatus.INTERNAL_SERVER_ERROR));

		EntityExchangeResult<APIResponse> returnResult = webClient.post().uri(API_ENDPOINT_ADD_ITEMS_NEW_BASKET)
				.contentType(MediaType.APPLICATION_JSON).header(API_HEADER_AUTHORIZATION, getGuestUserToken())
				.bodyValue(basket).exchange().expectStatus().is5xxServerError().expectBody(APIResponse.class)
				.returnResult();

		Assertions.assertEquals(returnResult.getResponseBody().getStatus().name(), ResponseStatus.ERROR.name());

	}

	@Test
	void testAddItemsToBasketWithGuestUserInvalidProduct() {

		log.info("Test:: testAddItemsToBasketWithGuestUserValidProduct :: started");

		BasketItemsDTO basket = createAddItemRequest(PRODUCT_ID);
		String url = cartProperties.getBaseUrl() + cartProperties.getAddItemsUri();
		String addItemUrl = url.replace(CartConstants.PARAM_BASET_ID, BASKET_ID);

		log.info("Test:: testCreateBasketWithGuest :: started");
		Mockito.when(sfccClient.getGuestToken()).thenReturn("token");
		Mockito.when(sfccRestClient.getResponseWithToken(addItemUrl, HttpMethod.POST,
				new HttpEntity<>(basket.getItemList()), CartResponseDTO.class))
				.thenReturn(new ResponseEntity<>(CartTestUtil.getCartResponseDTO(), HttpStatus.CREATED));

		EntityExchangeResult<APIResponse> addItems = webClient.post().uri(API_ENDPOINT_ADD_ITEMS_WITH_BASKET_ID, BASKET_ID)
				.contentType(MediaType.APPLICATION_JSON).header(API_HEADER_AUTHORIZATION, getGuestUserToken())
				.bodyValue(basket).exchange().expectStatus().is2xxSuccessful().expectBody(APIResponse.class)
				.returnResult();
		Assertions.assertNotNull(addItems.getResponseBody());

		Assertions.assertEquals(addItems.getResponseBody().getStatus().name(), ResponseStatus.SUCCESS.name());
	}

	public BasketItemsDTO createAddItemRequest(String productId) {
		ProductDTO dto = new ProductDTO();
		dto.setProductId(productId);
		dto.setQuantity(1);

		BasketItemsDTO basket = new BasketItemsDTO();
		basket.setItemList(Arrays.asList(dto));
		return basket;
	}
}
