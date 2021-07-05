package com.bernardguiang.SnackOverflow.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.CategoryDTO;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	CategoryRepository categoryRepository;
	
	public CategoryDTO save(CategoryDTO categoryDTO) 
	{
		Category category = categoryDTOToEntity(categoryDTO);
		Category saved = categoryRepository.save(category);
		CategoryDTO categoryDTOSaved = categoryEntityToDTO(saved);
		
		return categoryDTOSaved;
	}
	
	public List<CategoryDTO> findAll() 
	{
		Iterable<Category> categoriesIterator =  categoryRepository.findAll();
		
		List<CategoryDTO> categoryDTOs = new ArrayList<>();
		for(Category category : categoriesIterator)
		{
			CategoryDTO categoryDTO = categoryEntityToDTO(category);
			categoryDTOs.add(categoryDTO);
		}
		
		return categoryDTOs;
	}
	
	private CategoryDTO categoryEntityToDTO(Category category)
	{
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setId(category.getId());
		categoryDTO.setName(category.getName());
		
		return categoryDTO;
	}
	
	private Category categoryDTOToEntity(CategoryDTO categoryDTO)
	{
		Category category = new Category();
		category.setId(categoryDTO.getId());
		category.setName(categoryDTO.getName());
		
		return category;
	}
}
