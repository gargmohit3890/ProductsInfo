package com.sainsbury.serverside.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.sainsbury.serverside.vo.Product;
import com.sainsbury.serverside.vo.Total;

public class SainsburyExtractInfoUtility {
	
	private static String baseUrl;
	private static WebClient client = new WebClient();
	
	public static String noProductFound(final String url){
		baseUrl = url;
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		
		try {
			HtmlPage page = client.getPage(baseUrl);
			@SuppressWarnings("unchecked")
			List<HtmlElement> products = (List<HtmlElement>) page.getByXPath("//li[@class='gridItem']");
			if (products.isEmpty()) {
				return "No products found !";
			}
		}catch (Exception e) {
			return "Error scenario";
		}finally {
			client.close();
		}
		return null;
		
	}
	public static List<Product> scrapeProductInfo(){
		HtmlPage page;
		List<Product> productListInAPage = new ArrayList<Product>();
		
		try {
			page = client.getPage(baseUrl);
			@SuppressWarnings("unchecked")
			List<HtmlElement> products = (List<HtmlElement>) page.getByXPath("//li[@class='gridItem']");
			for (HtmlElement product : products) {
				Product productWithInformation = new Product();
				HtmlAnchor itemAnchor = ((HtmlAnchor) product.getFirstByXPath(".//a"));
				String productLinkUri = itemAnchor.getHrefAttribute();
				BigDecimal productPrice = getProductPrice(product);
				String productDetailUrl = getProductDetailsUrl(productLinkUri);
				HtmlPage productDetailPage = client.getPage(productDetailUrl);
				productWithInformation.setDescription(getDescriptionOfProduct(productDetailPage));
				productWithInformation.setCal_per_100g(getCaloriesInfo(productDetailPage));
				productWithInformation.setTitle(itemAnchor.asText());
				productWithInformation.setUnit_price(productPrice);
				productListInAPage.add(productWithInformation);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
		finally {
			client.close();
		}
		return productListInAPage;
	}
	
	public static Total calculateTotal(final List<Product> productListInAPage){
		return getTotalPriceOfAllProducts(productListInAPage);
	}
	/**
	 * @param productListInAPage
	 * @return
	 */
	private static Total getTotalPriceOfAllProducts(List<Product> productListInAPage) {
		BigDecimal sum = productListInAPage.stream().map(Product::getUnit_price).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		BigDecimal actualPrice = sum.divide(new BigDecimal(1.2), 2, RoundingMode.HALF_UP);
		BigDecimal vat = sum.subtract(actualPrice);
		Total total = new Total();
		total.setGross(sum);
		total.setVat(vat);
		return total;
	}

	/**
	 * This method is used to extract the information of the calories per 100g
	 * of the product
	 * 
	 * @param productWithInformation
	 * @param productDetailPage
	 */
	private static Integer getCaloriesInfo(HtmlPage productDetailPage) {
		HtmlTable table = null;
		try {
			table = (HtmlTable) productDetailPage.getHtmlElementById("information")
					.getFirstByXPath("//table[@class='nutritionTable']");

			String cal = null;
			if (table != null && table.asXml() != null) {
				HtmlTableBody body = table.getBodies().get(0);
				List<HtmlTableRow> rows = body.getRows();
				HtmlTableRow row0 = rows.get(0);
				boolean rowspan = row0.getCell(0).hasAttribute("rowspan");
				HtmlTableRow row1 = rows.get(1);
				if (rowspan) {

					// row1.getCol
					cal = row1.getCell(0).asText();
					if (cal != null && !"".equals(cal)) {
						cal = cal.replaceAll("\\D+", "");
						if (!cal.equals("")) {
							return Integer.parseInt(cal);
						}

					}
				} else {
					cal = row1.getCell(1).asText();
					return Integer.parseInt(cal);
				}

			}
		} catch (ElementNotFoundException ex) {
			System.out.println("Exception while fetching Calories");
			return null;
		}

		return null;
	}

	/**
	 * This method is used to extract the description of the product.
	 * 
	 * @param productWithInformation
	 * @param productDetailPage
	 */
	@SuppressWarnings("unchecked")
	private static String getDescriptionOfProduct(HtmlPage productDetailPage) {
		HtmlElement description = null;
		try {
			description = (HtmlElement) productDetailPage.getHtmlElementById("information").getFirstByXPath(".//p");
			;
			List<HtmlElement> listOfDescription = null;
			String descriptionText;
			if (description.asText() == null || "".equals(description.asText().trim())) {
				listOfDescription = (List<HtmlElement>) productDetailPage.getHtmlElementById("information")
						.getByXPath(".//p");

				for (HtmlElement desc : listOfDescription) {
					descriptionText = desc.asText();
					if (!"".equals(descriptionText)) {
						return descriptionText;
					}
				}

			} else {
				return description.asText();
			}
		} catch (ElementNotFoundException ex) {
			System.out.println("Exception while fetching description");
			return null;
		}

		return "";
	}

	/**
	 * This method is used to get the price in the desired format for the
	 * display. This method also takes care of rounding off.
	 * 
	 * @param product
	 * @return
	 */
	private static BigDecimal getProductPrice(HtmlElement product) {
		HtmlElement price = ((HtmlElement) product.getFirstByXPath(".//p[@class='pricePerUnit']"));
		// removing the pound sign
		String priceWithSign = price.asText().substring(1);
		// getting the position of the '/' to remove the string content
		int poistionForSubString = priceWithSign.indexOf("/");
		return new BigDecimal(priceWithSign.substring(0, poistionForSubString)).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * This method is used to create the url for getting the product detailed
	 * information.
	 * 
	 * @param productDetailUri
	 * @return
	 */
	private static String getProductDetailsUrl(String productDetailUri) {

		String s = "../";
		int t = StringUtils.lastIndexOf(productDetailUri, s);
		String suffixForProductDetails = productDetailUri.substring(t + 3);
		int indexOfLastSlashInMainUrl = StringUtils.lastIndexOf(baseUrl, "/");
		String finalUrl = baseUrl.substring(0, indexOfLastSlashInMainUrl);
		StringBuilder sb = new StringBuilder(finalUrl);
		String rever = sb.reverse().toString();
		int h = StringUtils.ordinalIndexOf(rever, "/", 6);
		String intialUrl = rever.substring(h);
		StringBuilder sb1 = new StringBuilder(intialUrl);
		return sb1.reverse() + suffixForProductDetails;
	}



}
