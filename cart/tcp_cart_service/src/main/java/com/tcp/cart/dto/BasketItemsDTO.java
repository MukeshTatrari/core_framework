package com.tcp.cart.dto;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasketItemsDTO {

	@Valid
	private List<ProductDTO> itemList;
}
