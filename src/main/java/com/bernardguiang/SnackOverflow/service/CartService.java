package com.bernardguiang.SnackOverflow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponseItem;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;

@Service
public class CartService {

	private final ProductRepository productRepository;
	
	@Autowired
	public CartService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public CartInfoResponse getCartInfo(CartRequest cartRequest) {
		List<CartInfoResponseItem> cartItems = new ArrayList<>();

		for(CartInfoRequestItem item : cartRequest.getItems()) {
			Product product =  productRepository.findById(item.getProductId())
					.orElseThrow(() -> new IllegalStateException("Could not find product " + item.getProductId()));
			ProductDTO productInfo = new ProductDTO(product);
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
