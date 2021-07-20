package com.bernardguiang.SnackOverflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.Cart;
import com.bernardguiang.SnackOverflow.dto.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.service.CartService;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/cart")
public class CartController {

	private final CartService cartService;
	
	@Autowired
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("/info")
	public Cart getCartInfo(@RequestBody List<CartInfoRequestItem> cartItems) {
		Cart cart = cartService.getCartInfo(cartItems);
		
		return cart;
	}
}
