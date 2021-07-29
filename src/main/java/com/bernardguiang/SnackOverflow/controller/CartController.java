package com.bernardguiang.SnackOverflow.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
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
	public CartInfoResponse getCartInfo(@RequestBody List<@Valid CartInfoRequestItem> cartItems) {
		CartInfoResponse cart = cartService.getCartInfo(cartItems);
		
		return cart;
	}
}
