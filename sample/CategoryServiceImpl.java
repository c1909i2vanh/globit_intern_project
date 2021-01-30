package com.globits.offerpro.service.impl;

import java.util.List;
import java.util.UUID;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.offerpro.domain.Category;
import com.globits.offerpro.domain.ProductCategory;
import com.globits.offerpro.dto.CategoryDto;
import com.globits.offerpro.dto.searchdto.CategorySearchDto;
import com.globits.offerpro.repository.CategoryRepository;
import com.globits.offerpro.repository.ProductCategoryRepository;
import com.globits.offerpro.service.CategoryService;

@Service
public class CategoryServiceImpl extends GenericServiceImpl<Category, UUID> implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ProductCategoryRepository productCategoryRepository;

	@Override
	public CategoryDto saveOne(CategoryDto dto, UUID id) {
		if(dto != null){
			Category entity = null;
			if(id != null){
				if(dto.getId() != null && !dto.getId().equals(id))
					return null;
				entity = categoryRepository.getOne(id);
			}
			if(entity == null)
				entity = new Category();

			/* Set all the values */
			entity.setName(dto.getName());
			entity.setCode(dto.getCode());
			entity = categoryRepository.save(entity);
			if(entity != null)
				return new CategoryDto(entity);
		}

		return null;
	}

	@Override
	public CategoryDto getOne(UUID id) {
		Category entity = categoryRepository.getOne(id);

		if(entity != null)
			return new CategoryDto(entity);

		return null;
	}

	@Override
	public Page<CategoryDto> searchByPage(CategorySearchDto dto) {
		if(dto == null)
			return null;

		int pageIndex = dto.getPageIndex();
		int pageSize = dto.getPageSize();

		if(pageIndex > 0)
			pageIndex--;
		else
			pageIndex = 0;

		String whereClause = "";

		String orderBy = " ORDER BY entity.name DESC";
		if(dto.getOrderBy() != null && StringUtils.hasLength(dto.getOrderBy().toString()))
			orderBy = " ORDER BY entity." + dto.getOrderBy() + " ASC";

		String sqlCount = "select count(entity.id) from Category as entity where (1=1)";
		String sql = "select new com.globits.offerpro.dto.CategoryDto(entity) from Category as entity where (1=1)";

		if(dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword()))
			whereClause += " AND ( UPPER(entity.name) LIKE UPPER(:text) OR UPPER(entity.code) LIKE UPPER(:text) )";

		sql += whereClause + orderBy;
		sqlCount += whereClause;

		Query q = manager.createQuery(sql, CategoryDto.class);
		Query qCount = manager.createQuery(sqlCount);

		if(dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())){
			q.setParameter("text", '%' + dto.getKeyword() + '%');
			qCount.setParameter("text", '%' + dto.getKeyword() + '%');
		}

		int startPosition = pageIndex * pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<CategoryDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();

		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<CategoryDto> result = new PageImpl<>(entities, pageable, count);
		return result;
	}

	@Override
	public Page<CategoryDto> searchCategoryByTypeOfOffer(CategorySearchDto dto, UUID agencyId) {
		if(dto == null)
			return null;

		int pageIndex = dto.getPageIndex();
		int pageSize = dto.getPageSize();

		if(pageIndex > 0)
			pageIndex--;
		else
			pageIndex = 0;

		String whereClause = "";

		String orderBy = "ORDER BY C.name DESC ";

		String sql = "SELECT new com.globits.offerpro.dto.CategoryDto(C) FROM Category AS C WHERE (1=1) AND C.id IN ( ";
		String sqlCount = "SELECT count(C.id) FROM Category AS C WHERE (1=1) AND C.id IN ( ";

		// select categoryId from offer
		String sqlDistinct = "SELECT DISTINCT PC.category.id FROM ProductCategory PC "
				+ "JOIN Offer O ON O.product.id = PC.product.id "
				+ "JOIN ProductPayout PP ON PC.product.id = PP.product.id " + "JOIN Agency A ON A.id = O.agency.id "
				+ "WHERE O.isPublicPayout = :isPublicPayout " + "AND O.agency.id = :agencyId "
				+ "AND PP.isCurrent = true " + "AND A.userManage.id IS NOT NULL " + "AND O.isShow = true "
				+ "AND O.parent IS NULL ";

		sqlDistinct += whereClause;
		sql += sqlDistinct + ") " + orderBy;
		sqlCount += sqlDistinct + ") ";

		Query q = manager.createQuery(sql, CategoryDto.class);
		Query qCount = manager.createQuery(sqlCount);

		q.setParameter("agencyId", agencyId);
		qCount.setParameter("agencyId", agencyId);

		System.out.println(dto.getIsPublicPayout());
		if(dto.getIsPublicPayout() != null){
//			System.out.println(dto.getIsPublicPayout());
			q.setParameter("isPublicPayout", dto.getIsPublicPayout());
			qCount.setParameter("isPublicPayout", dto.getIsPublicPayout());
		}

		int startPosition = pageIndex * pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<CategoryDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();

		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<CategoryDto> result = new PageImpl<>(entities, pageable, count);
		return result;
	}

	@Override
	public void deleteById(UUID id) {
		categoryRepository.deleteById(id);

	}

	@Override
	public boolean checkCodeWasUsed(String code, UUID id) {
		List<Category> category = categoryRepository.findByCode(code);
		if(category != null && category.size() > 0 && category.get(0) != null && category.get(0).getId() != null){
			if(id != null && StringUtils.hasText(id.toString()))
				if(category.get(0).getId().equals(id))
					return false;
			return true;
		}
		return false;
	}

	@Override
	public boolean checkNameWasUsed(String name, UUID id) {
		List<Category> category = categoryRepository.findByName(name);
		if(category != null && category.size() > 0 && category.get(0) != null && category.get(0).getId() != null){
			if(id != null && StringUtils.hasText(id.toString()))
				if(category.get(0).getId().equals(id))
					return false;
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteCheckById(UUID id) {
		List<ProductCategory> listP = productCategoryRepository.findAll();
		for(ProductCategory p: listP)
			if(id != null && id.equals(p.getCategory().getId()))
				return false;
		Long count = categoryRepository.checkcategory(id);
		if(count == null || count <= 0)
			return true;
		return false;
	}
}
