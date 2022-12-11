package com.tcp.cart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class CartRequestDTO implements Serializable {

	private static final long serialVersionUID = -3829235952771814149L;

	@NotNull(message = "Cart Id cannot be null")
	private Long cartId;

	@NotBlank(message = "Product Name cannot be blank")
	private String productName;

}
