package com.bernardguiang.SnackOverflow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bernardguiang.SnackOverflow.dto.CategoryDTO;
import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

@Service
public class DatabaseSeeder {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CategoryService categoryService;
	private final ProductService productService;
	private final Environment env;
	
	@Autowired
	public DatabaseSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder,
			CategoryService categoryService, ProductService productService, Environment env) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.categoryService = categoryService;
		this.productService = productService;
		this.env = env;
	}

	@EventListener(classes = { ContextRefreshedEvent.class})
	public void setDefaultUsers() {
		
		User user = new User();
		user.setUsername("admin");
		user.setEmail("admin@admin.com");
		user.setPassword(passwordEncoder.encode("password"));
		user.setRole(ApplicationUserRole.ADMIN.name());
		user.setFullName("Admin Guy");
		User savedAdmin = userRepository.save(user);
		
		User customer = new User();
		customer.setUsername("customer");
		customer.setEmail("customer@customer.com");
		customer.setPassword(passwordEncoder.encode("password"));
		customer.setRole(ApplicationUserRole.CUSTOMER.name());
		customer.setFullName("Customer Guy");
		User savedCustomer = userRepository.save(customer);
	}

	@EventListener(classes = { ContextRefreshedEvent.class})
	public void setInitialProducts() {
		
		CategoryDTO cdto1 = new CategoryDTO();
		cdto1.setName("Japan");
		CategoryDTO category1 = categoryService.save(cdto1);
		
		CategoryDTO cdto2 = new CategoryDTO();
		cdto2.setName("Korea");
		CategoryDTO category2 = categoryService.save(cdto2);
		
		CategoryDTO cdto3 = new CategoryDTO();
		cdto3.setName("USA");
		CategoryDTO category3 = categoryService.save(cdto3);
		
		CategoryDTO cdto4 = new CategoryDTO();
		cdto4.setName("Australia");
		CategoryDTO category4 = categoryService.save(cdto4);
		
		List<String> categories = new ArrayList<>();
		categories.add(category1.getName());
		categories.add(category2.getName());
		categories.add(category3.getName());
		categories.add(category4.getName());
		
		List<String> descriptions = new ArrayList<>();
		descriptions.add("Duis nec nisi neque. Quisque imperdiet ultrices nunc, vitae rutrum sem eleifend quis. Cras id aliquam purus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Integer tempor enim tellus, at hendrerit mi molestie consequat. Morbi sit amet justo a eros maximus tincidunt sed sit amet elit. Nulla facilisi. Sed aliquam lacus nisl, ac interdum dolor sollicitudin elementum. Quisque faucibus rhoncus lacus, quis rhoncus urna pretium eu. Nunc ac commodo mauris, in laoreet ante. Integer et fringilla odio.");
		descriptions.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer molestie justo eget sem sollicitudin, maximus faucibus lectus pharetra. Etiam pellentesque nisl in tristique laoreet. Sed lacus ligula, tempor sit amet lacus quis, egestas mollis ligula. Mauris dignissim condimentum nunc efficitur fringilla. Duis maximus in lorem eget dignissim. Nulla elementum massa magna, et faucibus lectus varius et. Duis non nibh pretium, suscipit erat eleifend, porttitor mauris. Nunc consectetur risus eget ante tempor vehicula. Nam posuere nunc vel commodo lobortis.");
		descriptions.add("In cursus risus orci, eu elementum turpis interdum id. Donec eu consectetur nunc, a blandit dolor. Nam leo arcu, convallis quis venenatis ut, mattis ac sapien. Etiam sed aliquam dui, quis dictum ante. Praesent ac massa sed quam consectetur ullamcorper. Proin lacus ipsum, tincidunt in nulla vel, euismod tristique urna. Quisque maximus varius ullamcorper.");
		descriptions.add("Sed vel quam vitae odio tempor porta. Vivamus in ante neque. Fusce ultrices arcu sed nulla vehicula congue. Etiam et semper massa, id sodales ante. Sed gravida massa nec arcu finibus finibus. Fusce faucibus, enim ut ultrices malesuada, leo arcu sagittis lacus, sit amet pharetra lectus felis non erat. Etiam vel turpis leo. Sed fringilla dui est, non efficitur tellus feugiat eu. Fusce convallis nulla justo, sit amet iaculis dolor dignissim a. Morbi et eros ac metus hendrerit bibendum et non nulla. Aenean lacinia elementum tellus dapibus vehicula.");
		descriptions.add("Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Etiam sollicitudin convallis mauris, id pulvinar elit cursus vitae. Donec facilisis erat tortor, vel lacinia massa maximus id. In nulla orci, rutrum ac blandit id, auctor et lorem. Donec facilisis, neque in sagittis sollicitudin, leo velit tempor nulla, vitae varius nibh justo ut diam. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ut ipsum nec arcu rhoncus volutpat. Vivamus porttitor nisl eu libero ultricies, quis consequat purus cursus. Nulla ut augue suscipit, efficitur augue ut, blandit mi. Proin vitae nisi vitae quam semper sagittis. Sed ac eros vel tellus egestas lacinia. Suspendisse potenti. Proin sem dui, imperdiet eget nulla sed, consectetur ornare elit.");
		
		List<BigDecimal> prices = new ArrayList<>();
		prices.add(new BigDecimal("1.00"));
		prices.add(new BigDecimal("2.00"));
		prices.add(new BigDecimal("3.50"));
		prices.add(new BigDecimal("10.00"));
		prices.add(new BigDecimal("7.00"));
		prices.add(new BigDecimal("5.00"));
		
		RestTemplate restTemplate = new RestTemplate();
		String unsplashAccessToken = env.getProperty("unsplash_access_token");
		ResponseEntity<String> response = 
				restTemplate.getForEntity("https://api.unsplash.com/search/photos?query=snack&client_id=" + unsplashAccessToken +"&per_page=30", String.class);
		
		String responseString = response.getBody();
		
	
		try {
		     JSONObject responseObject = new JSONObject(responseString);
		     JSONArray results = responseObject.getJSONArray("results");
		     for(int i = 0; i < results.length(); i++) {
		    	JSONObject current = results.getJSONObject(i);
		    	JSONObject urls = current.getJSONObject("urls");
		    	String url = urls.getString("small");
		    	
		    	ProductDTO productDTO = new ProductDTO();
		    	productDTO.setName(current.getString("alt_description"));
		 		List<String> productCategories = new ArrayList<>();
		 		productCategories.add((String) getRandom(categories)); // random category from collection
		 		productDTO.setDescription((String) getRandom(descriptions));
		 		productDTO.setPrice((BigDecimal) getRandom(prices));
		 		productDTO.setCategories(productCategories);
		 		List<String> images = new ArrayList<>();
		 		images.add(url);
		 		productDTO.setImages(images);
		 		productService.save(productDTO);
		     }
		}catch (JSONException err){
		     err.printStackTrace();
		}
	}
	
	public static <T> T getRandom(Collection<T> coll) {
	    int num = (int) (Math.random() * coll.size());
	    for(T t: coll) if (--num < 0) return t;
	    throw new AssertionError();
	}
}
