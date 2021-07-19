package com.bernardguiang.SnackOverflow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.Cart;
import com.bernardguiang.SnackOverflow.dto.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.CartItem;
import com.bernardguiang.SnackOverflow.dto.ProductDTO;

@Service
public class CartService {

	private final ProductService productService;
	
	public CartService(ProductService productService) {
		this.productService = productService;
	}
	
	public Cart getCartInfo(List<CartInfoRequestItem> cartInfoRequestItems) {
		
		List<CartItem> cartItems = new ArrayList<>();

		for(CartInfoRequestItem item : cartInfoRequestItems) {
			ProductDTO productInfo = productService.findById(item.getProductId());
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(item.getQuantity());
			cartItem.setProduct(productInfo);
			cartItems.add(cartItem);
		}
		return new Cart(getCartTotal(cartItems), cartItems);
	}
	
	private BigDecimal getCartTotal(List<CartItem> cartItems) {
		BigDecimal total = new BigDecimal("0");
		for(CartItem item : cartItems) {
			total = total.add(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
		}
		
		return total;
	}
}
