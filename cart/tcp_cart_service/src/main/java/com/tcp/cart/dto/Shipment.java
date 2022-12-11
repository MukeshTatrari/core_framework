package com.tcp.cart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Shipment implements Serializable{

	private static final long serialVersionUID = 6160188073754816490L;
	
	private double adjustedMerchandizeTotalTax;
	private double adjustedShippingTotalTax;
	private boolean gift;
	private double merchandizeTotalTax;
	private double productSubTotal;
	private double productTotal;
	private String shipmentId;
	private double shipmentTotal;
	private String shippingStatus;
	private double shippingTotal;
	private double shippingTotalTax;
	private double taxTotal;

}
