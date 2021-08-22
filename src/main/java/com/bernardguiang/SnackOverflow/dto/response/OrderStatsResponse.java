package com.bernardguiang.SnackOverflow.dto.response;

import java.math.BigDecimal;

public class OrderStatsResponse {
	// Orders total
	private int successfulOrders;
	// excluded orders total
	private int unsuccessfulOrders;
	// Income total
	private BigDecimal totalIncome;
	// excluded income total
	private BigDecimal unsuccessfulPayments;
	
	public OrderStatsResponse(int successfulOrders, int unsuccessfulOrders, BigDecimal totalIncome,
			BigDecimal unsuccessfulPayments) {
		this.setSuccessfulOrders(successfulOrders);
		this.setUnsuccessfulOrders(unsuccessfulOrders);
		this.setTotalIncome(totalIncome);
		this.setUnsuccessfulPayments(unsuccessfulPayments);
	}
	public int getSuccessfulOrders() {
		return successfulOrders;
	}
	public void setSuccessfulOrders(int successfulOrders) {
		this.successfulOrders = successfulOrders;
	}
	public int getUnsuccessfulOrders() {
		return unsuccessfulOrders;
	}
	public void setUnsuccessfulOrders(int unsuccessfulOrders) {
		this.unsuccessfulOrders = unsuccessfulOrders;
	}
	public BigDecimal getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}
	public BigDecimal getUnsuccessfulPayments() {
		return unsuccessfulPayments;
	}
	public void setUnsuccessfulPayments(BigDecimal unsuccessfulPayments) {
		this.unsuccessfulPayments = unsuccessfulPayments;
	}
	
}
