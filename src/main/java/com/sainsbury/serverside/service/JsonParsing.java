package com.sainsbury.serverside.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sainsbury.serverside.vo.Output;
import com.sainsbury.serverside.vo.Product;
import com.sainsbury.serverside.vo.Total;

public class JsonParsing {
	
	public static void createOutput(List<Product> productListInAPage, Total total) throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		Output output = new Output();
		output.setResults(productListInAPage);
		output.setTotal(total);
		System.out.println(mapper.writeValueAsString(output));
		
	}

}
