package com.tcp.cart.service;

import com.tcp.cart.dto.BasketItemsDTO;
import com.tcp.cart.dto.CartResponseDTO;
import com.tcp.cart.sfcc.client.CartClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * CartServiceImpl will be responsible for handling business logic, any kind of
 * transformation or orchestration. For data CartServiceImpl will make call to
 * CartDAO.
 * 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

	private final CartClient cartClient;

	/**
	 * @param basketId
	 * @return CartResponseDTO
	 */
	@Override
	public CartResponseDTO getCart(String basketId) {
		log.debug("Inside CartServiceImpl::getCart basket id: {}", basketId);
		return cartClient.getCart(basketId);
	}

	/**
	 * @return CartResponseDTO
	 */
	@Override
	public CartResponseDTO createCart() {
		log.debug("Inside ::createCart : {}");
		return cartClient.createCart();
	}
	
	/**
	 * 
	 * @param basketId
	 * @param items
	 * @return CartResponseDTO
	 */

	@Override
	public CartResponseDTO addItemsToBasket(String basketId, BasketItemsDTO items) {
		return cartClient.addItemsToBasket(basketId, items);
	}
}
