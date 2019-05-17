package com.sainsbury.serverside.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Product {

	private String title;
    private Integer cal_per_100g ;
    private BigDecimal unit_price;
    private String description;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the cal_per_100g
	 */
	public Integer getCal_per_100g() {
		return cal_per_100g;
	}
	/**
	 * @param cal_per_100g the cal_per_100g to set
	 */
	public void setCal_per_100g(Integer cal_per_100g) {
		this.cal_per_100g = cal_per_100g;
	}
	public BigDecimal getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(BigDecimal unit_price) {
		this.unit_price = unit_price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Product [title=" + title + ", cal_per_100g=" + cal_per_100g + ", unit_price=" + unit_price
				+ ", description=" + description + "]";
	}
	
	
}
