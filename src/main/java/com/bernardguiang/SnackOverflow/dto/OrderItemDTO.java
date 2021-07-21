package com.bernardguiang.SnackOverflow.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.bernardguiang.SnackOverflow.model.OrderItem;

public class OrderItemDTO {
	private Long id;
	private ProductDTO product;
	@NotNull
	private BigDecimal price;
	@Min(1)
	private int quantity;
	
	public OrderItemDTO() {
	}
	
	public OrderItemDTO(OrderItem orderItem) {
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
