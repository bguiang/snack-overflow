package com.bernardguiang.SnackOverflow.service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.repository.CategoryRepository;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;

@Service
public class ProductService 
{
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	
	@Autowired
	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}
	 
	public ProductDTO save(ProductDTO productDTO)
	{
		Product product = dtoToProduct(productDTO);
		Product saved = productRepository.save(product);
		ProductDTO productDTOSaved = new ProductDTO(saved);
		
		return productDTOSaved;
	}
	
	public ProductDTO findById(long id){
		Product product =  productRepository.findById(id).orElseThrow(() -> new IllegalStateException("Could not find product " + id));
		return new ProductDTO(product);
	}
	
	public List<ProductDTO> findAll(){
		Iterable<Product> productsIterator =  productRepository.findAll();
		
		List<ProductDTO> productDTOs = new ArrayList<>();
		for(Product product : productsIterator)
		{
			ProductDTO productDTO = new ProductDTO(product);
			productDTOs.add(productDTO);
		}
		
		return productDTOs;
	}
	
	public List<ProductDTO> findAllByCategoryName(String categoryName)
	{
		List<ProductDTO> productDTOs = new ArrayList<>();
		
		Optional<Category> category = categoryRepository.findByName(categoryName);
		if(category.isPresent())
		{
			for(Product product: category.get().getProducts())
			{
				productDTOs.add(new ProductDTO(product));
			}
		}
		
		return productDTOs;
	}
	
	// do not expose DTO -> Entity Logic as it could accidentally lead to updating the entity when saving using data from the client
	// Use repository to find by id
	private Product dtoToProduct(ProductDTO productDTO)
	{
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setImages(productDTO.getImages());
		
		// Should it throw an error if category doesn't exist?
		Set<Category> categories = new HashSet<>();
		for(String categoryName: productDTO.getCategories())
		{
			Optional<Category> category = categoryRepository.findByName(categoryName);
			if(category.isPresent())
			{
				categories.add(category.get());
			}
		}
		
		return product;
	}
}
