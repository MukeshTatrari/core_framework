package com.tcp.cart.sfcc.client;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.tcp.cart.config.CartProperties;
import com.tcp.framework.sfcc.client.SFCCRestClient;

public class CartClientTest {

	private static final String BASKET_ID = "abc123";

	@Mock
	public CartProperties properties;

	@InjectMocks
	public CartClient cartClient;

	@Mock
	public SFCCRestClient restClient;

//	@Mock
//	private TokenInitializer tokenInitializer;
//
//	@Before
//	public void init() throws IOException {
//		MockitoAnnotations.initMocks(this);
//	}
//
//	@Test
//	public void getCartById() {
//
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
//		responseHeaders.add(AUTHORIZATION, BEARER_TOKEN);
//
//		HttpHeaders getCartHeaders = new HttpHeaders();
//		getCartHeaders.setContentType(MediaType.APPLICATION_JSON);
//		getCartHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//		getCartHeaders.setBearerAuth("asdjkashdhas");
//
//		CartResponseDTO responseDto = new CartResponseDTO();
//		responseDto.setBasketId(BASKET_ID);
//
//		HttpEntity<Map> basketEntity = new HttpEntity<Map>(null, getCartHeaders);
//		ResponseEntity<CartResponseDTO> basketResponse = new ResponseEntity<CartResponseDTO>(responseDto,
//				responseHeaders, HttpStatus.OK);
//
//		Mockito.when(tokenInitializer.getAuthToken()).thenReturn(CartTestConstants.BEARER_TOKEN);
//		Mockito.when(properties.getTokenUri()).thenReturn(LOGIN_URL);
//		Mockito.when(properties.getGetCartUri()).thenReturn(GET_BASKET_URL);
//		Mockito.when(properties.getBaseUrl()).thenReturn(BASE_URL);
//
//		Mockito.when(
//				restClient.getResponseWithToken(BASE_URL + GET_BASKET_URL, HttpMethod.GET,  new HttpEntity<>(RestUtil.buildCommonHeader()), CartResponseDTO.class))
//				.thenReturn(basketResponse);
//
//		CartResponseDTO basResponse = cartClient.getCart("123456");
//
//		assertEquals(BASKET_ID, basResponse.getBasketId());
//	}
//
//	@Test
//	public void createCart() {
//
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
//		responseHeaders.add(AUTHORIZATION, BEARER_TOKEN);
//
//		HttpHeaders getCartHeaders = new HttpHeaders();
//		getCartHeaders.setContentType(MediaType.APPLICATION_JSON);
//		getCartHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//		getCartHeaders.setBearerAuth("asdjkashdhas");
//
//		CartResponseDTO responseDto = new CartResponseDTO();
//		responseDto.setBasketId(BASKET_ID);
//
//		Map<String, String> body = new HashMap<String, String>();
//
//		HttpEntity<Map> basketEntity = new HttpEntity<Map>(body, getCartHeaders);
//
//		ResponseEntity<CartResponseDTO> basketResponse = new ResponseEntity<CartResponseDTO>(responseDto,
//				responseHeaders, HttpStatus.OK);
//
//		Mockito.when(properties.getCreateCartUri()).thenReturn(CREATE_BASKET_URL);
//		Mockito.when(tokenInitializer.getAuthToken()).thenReturn(CartTestConstants.BEARER_TOKEN);
//		Mockito.when(properties.getBaseUrl()).thenReturn(BASE_URL);
//		Mockito.when(restClient.getResponseWithToken(BASE_URL + CREATE_BASKET_URL, HttpMethod.POST, "{}",
//				CartResponseDTO.class)).thenReturn(basketResponse);
//
//		CartResponseDTO basResponse = cartClient.createCart();
//
//		assertEquals(BASKET_ID, basResponse.getBasketId());
//	}
//
//	@Test
//	public void buildHeaders() {
//		Mockito.when(tokenInitializer.getAuthToken()).thenReturn(CartTestConstants.BEARER_TOKEN);
//		HttpHeaders buildHeaders = RestUtil.buildCommonHeader();
//		assertEquals(MediaType.APPLICATION_JSON, buildHeaders.getContentType());
//	}
}