package com.tcp.cart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ProductItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2462114931900377066L;
	
	private double adjustedTax;
	private double basePrice;
	private boolean bonusProductLineItem;
	private boolean gift;
	private String itemId;
	private String itemText;
	private double price;
	private double priceAfterItemDiscount;
	private double priceAfterOrderDiscount;
	private String productId;
	private String productName;
	private int quantity;
	private String shipmentId;
	private double tax;
	private double taxBasis;
	private String taxClassId;
	private double taxRate;
}
