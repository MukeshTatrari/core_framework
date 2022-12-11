package com.tcp.cart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CustomerInfo implements Serializable {

	private static final long serialVersionUID = 3948021392587911854L;

	private String customerId;
	private String email;

}
