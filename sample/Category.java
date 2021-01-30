package com.globits.offerpro.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
@XmlRootElement
@Table(name = "tbl_categories")
@Entity
public class Category extends BaseObject {
	private static final long serialVersionUID = 1L;

	@Column(name="name")
	private String name;
	
	@Column(name="code")
	private String code;
	
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ProductCategory> productCategory;

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

	public Set<ProductCategory> getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(Set<ProductCategory> productCategory) {
		this.productCategory = productCategory;
	}
	

	
	
}

