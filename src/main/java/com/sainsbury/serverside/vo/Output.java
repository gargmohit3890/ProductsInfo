package com.sainsbury.serverside.vo;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Output {
	
	private List<Product> results;
	private Total total;

	/**
	 * @return the results
	 */
	public List<Product> getResults() {
		return results;
	}
	/**
	 * @param results the results to set
	 */
	public void setResults(List<Product> results) {
		this.results = results;
	}
	public Total getTotal() {
		return total;
	}
	public void setTotal(Total total) {
		this.total = total;
	}

}

