package com.tcp.cart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class CartResponseDTO implements Serializable {

	private static final long serialVersionUID = 6653983470581689438L;
	
	private double adjustedMerchandizeTotalTax;
	private double adjustedShippingTotalTax;
	private boolean agentBasket;
	private String basketId;
	private String channelType;
	private Date creationDate;
	private String currency;
	private CustomerInfo customerInfo;
	private Date lastModified;
	private double merchandizeTotalTax;
	private Notes notes;
	private double orderTotal;
	private double productSubTotal;
	private double productTotal;
	private List<Shipment> shipments;
	private List<ProductItem> productItems;
	private List<ShippingItem> shippingItems;
	private double shippingTotal;
	private double shippingTotalTax;
	private String taxation;
	private double taxTotal;

}
