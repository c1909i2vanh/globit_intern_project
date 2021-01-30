package com.globits.offerpro.rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.offerpro.Constants;
import com.globits.offerpro.dto.CategoryDto;
import com.globits.offerpro.dto.searchdto.CategorySearchDto;
import com.globits.offerpro.service.AgencyService;
import com.globits.offerpro.service.CategoryService;
import com.globits.security.service.UserService;

@RestController
@RequestMapping(path = "/api/category")
public class RestCategoryController {
	@Autowired
	UserService userService;

	@Autowired
	AgencyService agencyService;

	@Autowired
	CategoryService categoryService;

	@Secured({
			Constants.OFFERPRO_ADMIN, Constants.MAKETING_MANAGER, Constants.MAKETING_STAFF, Constants.ROLE_USER,
			Constants.BUSINESS_DEVELOP_STAFF, Constants.BUSINESS_DEVELOP_MANAGER, Constants.ROLE_FINANCIAL_MANAGER,
			Constants.ROLE_FINANCIAL_STAFF
	})
	@RequestMapping(value = "/searchByPage", method = RequestMethod.POST)
	public ResponseEntity<Page<CategoryDto>> searchByPage(@RequestBody CategorySearchDto searchDto) {
		Page<CategoryDto> page = null;

		// OfferList
//		if(searchDto.getIsPublicPayout() != null){
//			Long userID = userService.getCurrentUser().getId();
//			UUID agencyID = agencyService.getCurrentAgencyID(userID);
//
////			System.out.println(agencyID);
//			// public or private offer
//			page = categoryService.searchCategoryByTypeOfOffer(searchDto, agencyID);
//		} else
			page = categoryService.searchByPage(searchDto);

		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@Secured({
			Constants.OFFERPRO_ADMIN, Constants.MAKETING_MANAGER, Constants.MAKETING_STAFF, Constants.ROLE_USER,
			Constants.BUSINESS_DEVELOP_STAFF, Constants.BUSINESS_DEVELOP_MANAGER, Constants.ROLE_FINANCIAL_MANAGER,
			Constants.ROLE_FINANCIAL_STAFF
	})
	@RequestMapping(value = "/searchByPageCategoryOfOffer", method = RequestMethod.POST)
	public ResponseEntity<Page<CategoryDto>> searchByPageCategoryOfOffer(@RequestBody CategorySearchDto searchDto) {
		Long userID = userService.getCurrentUser().getId();
		UUID agencyID = agencyService.getCurrentAgencyID(userID);
		searchDto.setId(agencyID);

		Page<CategoryDto> page = categoryService.searchByPage(searchDto);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@Secured({
			Constants.OFFERPRO_ADMIN, Constants.MAKETING_MANAGER, Constants.MAKETING_STAFF,
			Constants.BUSINESS_DEVELOP_STAFF, Constants.BUSINESS_DEVELOP_MANAGER
	})
	@RequestMapping(value = "/check/codeWasUsed", method = RequestMethod.POST)
	public Boolean codeWasUsed(@RequestBody CategoryDto dto) {
		boolean result = true;
		if(dto.getCode() != null && StringUtils.hasText(dto.getCode()))
			result = categoryService.checkCodeWasUsed(dto.getCode(), dto.getId());
		return result;
	}

	@Secured({
			Constants.OFFERPRO_ADMIN, Constants.MAKETING_MANAGER, Constants.MAKETING_STAFF,
			Constants.BUSINESS_DEVELOP_STAFF, Constants.BUSINESS_DEVELOP_MANAGER
	})
	@RequestMapping(value = "/check/nameWasUsed", method = RequestMethod.POST)
	public Boolean nameWasUsed(@RequestBody CategoryDto dto) {
		boolean result = true;
		if(dto.getName() != null && StringUtils.hasText(dto.getName()))
			result = categoryService.checkNameWasUsed(dto.getName(), dto.getId());
		return result;
	}

	@Secured({
			Constants.OFFERPRO_ADMIN, Constants.MAKETING_MANAGER, Constants.MAKETING_STAFF,
			Constants.BUSINESS_DEVELOP_STAFF, Constants.BUSINESS_DEVELOP_MANAGER
	})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CategoryDto> getOne(@PathVariable("id") UUID id) {
		CategoryDto dto = categoryService.getOne(id);

		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@Secured({
			Constants.OFFERPRO_ADMIN, Constants.MAKETING_MANAGER, Constants.MAKETING_STAFF,
			Constants.BUSINESS_DEVELOP_STAFF, Constants.BUSINESS_DEVELOP_MANAGER
	})
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<CategoryDto> saveOne(@RequestBody CategoryDto dto) {
		CategoryDto result = categoryService.saveOne(dto, null);

		return new ResponseEntity<>(result, result != null? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@Secured({
			Constants.OFFERPRO_ADMIN, Constants.MAKETING_MANAGER, Constants.MAKETING_STAFF,
			Constants.BUSINESS_DEVELOP_STAFF, Constants.BUSINESS_DEVELOP_MANAGER
	})
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<CategoryDto> updateOne(@RequestBody CategoryDto dto, @PathVariable("id") UUID id) {
		CategoryDto result = categoryService.saveOne(dto, id);

		return new ResponseEntity<>(result, result != null? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@Secured({
			Constants.OFFERPRO_ADMIN, Constants.ROLE_MANAGER, Constants.BUSINESS_DEVELOP_STAFF,
			Constants.BUSINESS_DEVELOP_MANAGER
	})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteById(@PathVariable("id") UUID id) {
		categoryService.deleteById(id);

		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@Secured({
			Constants.OFFERPRO_ADMIN, Constants.ROLE_MANAGER, Constants.MAKETING_STAFF,
			Constants.BUSINESS_DEVELOP_STAFF, Constants.BUSINESS_DEVELOP_MANAGER
	})
	@RequestMapping(value = "/deletecheckMultiple/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteCheckID(@PathVariable("id") UUID id) {

		boolean result = categoryService.deleteCheckById(id);
		if(result){
			categoryService.deleteById(id);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}

}
