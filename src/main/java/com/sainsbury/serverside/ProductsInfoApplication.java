package com.sainsbury.serverside;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sainsbury.serverside.service.SainsburyScraper;

@SpringBootApplication
public class ProductsInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsInfoApplication.class, args);
		String baseUrl = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
		SainsburyScraper.run(baseUrl);
	}

}
