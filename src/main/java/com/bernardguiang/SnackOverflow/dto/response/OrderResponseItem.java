package com.bernardguiang.SnackOverflow.dto.response;

import java.math.BigDecimal;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.model.OrderItem;

// used as response as part of OrderResponse
public class OrderResponseItem {
	private Long id;
	private ProductDTO product;
	private BigDecimal price;
	private int quantity;

	public OrderResponseItem(OrderItem orderItem) {
		this.setId(orderItem.getId());
		ProductDTO productDTO = new ProductDTO(orderItem.getProduct());
		this.setProduct(productDTO);
		this.setPrice(orderItem.getPrice());
		this.setQuantity(orderItem.getQuantity());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	
}
