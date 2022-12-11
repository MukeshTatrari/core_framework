package com.tcp.cart.controller;

import static com.tcp.cart.constants.CartConstants.BASKET_ID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tcp.cart.dto.BasketItemsDTO;
import com.tcp.cart.dto.CartResponseDTO;
import com.tcp.cart.service.CartService;
import com.tcp.framework.response.APIResponse;
import com.tcp.framework.response.ResponseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CartController will be responsible for getting the
 * request(GET,POST,PUT,DELETE,PATCH) and sending response back, It will call
 * CartService Class for the further process.
 */
@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class CartController {

	private final CartService cartService;


	/**
	 * 
	 * @param basketId
	 * @param authorization
	 * @return
	 * 
	 *         Fetch Cart details on the basis of cart id and bearer token
	 */
	@GetMapping("/carts/{basketId}")
	public ResponseEntity<APIResponse> getCart(@PathVariable(name = BASKET_ID) String basketId,
			@RequestHeader(required = true) String authorization) {
		log.debug("-->getCart(), basketId: {}", basketId);
		CartResponseDTO cartResponseDto = cartService.getCart(basketId);
		log.debug("<--cartResponseDto: {} ", cartResponseDto);
		return new ResponseEntity<>(new APIResponse(cartResponseDto, ResponseStatus.SUCCESS), HttpStatus.OK);
	}

	/**
	 * 
	 * @param authorization
	 * @return
	 * 
	 *         Create cart on the basis of bearer token.
	 */
	@PostMapping("/carts")
	public ResponseEntity<APIResponse> createCart(@RequestHeader(required = true) String authorization) {
		log.debug("-->createCart()");
		CartResponseDTO response = cartService.createCart();
		log.debug("<--response id: {} ", response);
		return new ResponseEntity<>(new APIResponse(response, ResponseStatus.SUCCESS), HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param authorization
	 * @param basketId
	 * @param product
	 * @return
	 * 
	 *         add an item to basket
	 */
	@PostMapping("/items/{basketId}")
	public ResponseEntity<APIResponse> addItemsToBasket(@RequestHeader(required = true) String authorization,
			@PathVariable(name = BASKET_ID, required = true) String basketId,
			@Valid @RequestBody(required = true) BasketItemsDTO items) {
		log.debug("-->addItemsToBasket :::::: ");
		CartResponseDTO response = cartService.addItemsToBasket(basketId, items);
		log.debug("<--response : {} ", response);
		return new ResponseEntity<>(new APIResponse(response, ResponseStatus.SUCCESS), HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param authorization
	 * @param basketId
	 * @param product
	 * @return
	 * 
	 *         add an item to basket
	 */
	@PostMapping("/items")
	public ResponseEntity<APIResponse> addItemsToBasket(@RequestHeader(required = true) String authorization,
			@Valid @RequestBody(required = true) BasketItemsDTO items) {
		log.debug("-->addItemsToBasket :::::: ");
		CartResponseDTO basketResponse = cartService.createCart();
		CartResponseDTO response = cartService.addItemsToBasket(basketResponse.getBasketId(), items);

		log.debug("<--response : {} ", response);
		return new ResponseEntity<>(new APIResponse(response, ResponseStatus.SUCCESS), HttpStatus.CREATED);
	}

}
