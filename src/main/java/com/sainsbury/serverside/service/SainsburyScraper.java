package com.sainsbury.serverside.service;

import java.util.List;

import org.apache.commons.validator.UrlValidator;

import com.sainsbury.serverside.vo.Product;
import com.sainsbury.serverside.vo.Total;

/**
 * Hello world!
 *
 */
@SuppressWarnings("deprecation")
public class SainsburyScraper {

	public static String run(String url) {

		if (url == null || "".equals(url)) {
			return "Empty url";
		}
		if (!vaildateUrl(url)) {
			return "Invalid url";
		}
		try {
			String noProductFound = SainsburyExtractInfoUtility.noProductFound(url);
			if (noProductFound != null) {
				return noProductFound;
			}
			List<Product> productListInAPage = SainsburyExtractInfoUtility.scrapeProductInfo();
			Total total = SainsburyExtractInfoUtility.calculateTotal(productListInAPage);
			JsonParsing.createOutput(productListInAPage, total);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error scenario";
		}
		return "success";
	}

	private static boolean vaildateUrl(String url) {
		UrlValidator urlValidator = new UrlValidator();
		return urlValidator.isValid(url);
	}

}
