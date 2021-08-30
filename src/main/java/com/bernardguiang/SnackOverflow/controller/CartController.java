package com.bernardguiang.SnackOverflow.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

	private final CartService cartService;
	
	@Autowired
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("/info")
	public CartInfoResponse getCartInfo(@RequestBody @Valid CartRequest cartInfoRequest) {
		CartInfoResponse cart = cartService.getCartInfo(cartInfoRequest);
		
		return cart;
	}
}
