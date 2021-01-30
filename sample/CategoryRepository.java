package com.globits.offerpro.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.offerpro.domain.Category;
import com.globits.offerpro.domain.Product;
import com.globits.offerpro.domain.ProductCategory;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

	@Query("select c FROM Category c where c.code = ?1 " )
	List<Category> findByCode(String code);

	@Query("select c FROM Category c where c.name = ?1 " )
	List<Category> findByName(String name);
	
	@Query("select count(entity) FROM ProductCategory entity where entity.category.id =?1")
	Long checkcategory(UUID id);
	
	

	

}
