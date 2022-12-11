package com.tcp.cart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class ShippingItem implements Serializable {

	private static final long serialVersionUID = 4767730528094078217L;
	
	private double adjustedTax;
	private double basePrice;
	private String itemId;
	private String itemText;
	private double price;
	private double priceAfterItemDiscount;
	private String shipmentId;
	private double tax;
	private double taxBasis;
	private String taxClassId;
	private double taxRate;

}
