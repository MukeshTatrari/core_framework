package com.tcp.cart.util;

import java.util.Date;

import com.tcp.cart.dto.CartResponseDTO;

public class CartTestUtil {

	public static CartResponseDTO getCartResponseDTO() {
		CartResponseDTO cartResponseDTO = new CartResponseDTO();
		cartResponseDTO.setAdjustedMerchandizeTotalTax(1);
		cartResponseDTO.setAdjustedShippingTotalTax(10);
		cartResponseDTO.setAgentBasket(false);
		cartResponseDTO.setBasketId("1");
		cartResponseDTO.setChannelType("test");
		cartResponseDTO.setCreationDate(new Date());
		cartResponseDTO.setCurrency("USD");
		cartResponseDTO.setLastModified(new Date());
		cartResponseDTO.setMerchandizeTotalTax(10);
		cartResponseDTO.setOrderTotal(10);
		cartResponseDTO.setProductSubTotal(50);
		cartResponseDTO.setProductTotal(100);
		cartResponseDTO.setShippingTotal(5);
		cartResponseDTO.setShippingTotalTax(1);
		cartResponseDTO.setTaxTotal(100);
		return cartResponseDTO;
	}
}
