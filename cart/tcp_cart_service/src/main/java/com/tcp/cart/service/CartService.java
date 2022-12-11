package com.tcp.cart.service;

import com.tcp.cart.dto.BasketItemsDTO;
import com.tcp.cart.dto.CartResponseDTO;

public interface CartService {

	/**
	 * @param basketId
	 * @return CartResponseDTO
	 */
	public CartResponseDTO getCart(String basketId);

	/**
	 * @param cartRequestDto
	 * @return basketId
	 */
	public CartResponseDTO createCart();
	
	/**
	 * 
	 * @param basketId
	 * @param items
	 * @return
	 */
	public CartResponseDTO addItemsToBasket(String basketId, BasketItemsDTO items);
}
