package com.globits.offerpro.dto;

import java.util.HashSet;
import java.util.Set;

import com.globits.core.dto.BaseObjectDto;
import com.globits.offerpro.domain.Category;
import com.globits.offerpro.domain.Product;
import com.globits.offerpro.domain.ProductCategory;

public class CategoryDto extends BaseObjectDto {
	private String name;
	private String code;
	private Set<ProductDto> products;
	
	/* Getters and Setters */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Set<ProductDto> getProducts() {
		return products;
	}
	public void setProducts(Set<ProductDto> products) {
		this.products = products;
	}
	public CategoryDto() {
		super();
	}
	
	public CategoryDto(Category entity) {
		super();
		if (entity != null) {
			this.createDate = entity.getCreateDate();
			this.id = entity.getId();
			this.name = entity.getName();
			this.code = entity.getCode();
			
			if (entity.getProductCategory() != null && entity.getProductCategory().size() > 0) {
				this.products = new HashSet<ProductDto>();
				for (ProductCategory productCategory : entity.getProductCategory()) {
					ProductDto dto = new ProductDto(productCategory.getProduct(), true);
					this.products.add(dto);
				}
			}
		}
	}
	
	public CategoryDto(Category entity, boolean simple) {
		super();
		if (entity != null) {
			this.createDate = entity.getCreateDate();
			this.id = entity.getId();
			this.name = entity.getName();
			this.code = entity.getCode();
		}
	}
}
