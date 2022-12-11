package com.tcp.cart.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {

	@NotBlank(message = "Product Id is required")
	private String productId;

	@Min(value = 1, message = "Quantity must be greater than or equal to 1")
	private int quantity;

}
