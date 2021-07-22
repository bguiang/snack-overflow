package com.bernardguiang.SnackOverflow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponseItem;

@Service
public class CartService {

	private final ProductService productService;
	
	@Autowired
	public CartService(ProductService productService) {
		this.productService = productService;
	}
	
	public CartInfoResponse getCartInfo(List<CartInfoRequestItem> cartInfoRequestItems) {
		
		List<CartInfoResponseItem> cartItems = new ArrayList<>();

		for(CartInfoRequestItem item : cartInfoRequestItems) {
			ProductDTO productInfo = productService.findById(item.getProductId());
			CartInfoResponseItem cartItem = new CartInfoResponseItem();
			cartItem.setQuantity(item.getQuantity());
			cartItem.setProduct(productInfo);
			cartItems.add(cartItem);
		}
		return new CartInfoResponse(getCartTotal(cartItems), cartItems);
	}
	
	private BigDecimal getCartTotal(List<CartInfoResponseItem> cartItems) {
		BigDecimal total = new BigDecimal("0");
		for(CartInfoResponseItem item : cartItems) {
			total = total.add(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
		}
		
		return total;
	}
}
