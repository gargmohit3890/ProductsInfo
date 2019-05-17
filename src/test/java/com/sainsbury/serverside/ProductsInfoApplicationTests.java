package com.sainsbury.serverside;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gargoylesoftware.htmlunit.WebClient;
import com.sainsbury.serverside.service.SainsburyScraper;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductsInfoApplicationTests {

	private WebClient webClient;
	
	
	 
	@Before
	public void init() throws Exception {
	    webClient = new WebClient();
	}
	 
	@After
	public void close() throws Exception {
	    webClient.close();
	}
	

	@Test
	public void emptyUrl()
	  throws Exception {
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setJavaScriptEnabled(false);
	    String baseUrl = "";
	  
	    String result = SainsburyScraper.run(baseUrl);
	    Assert.assertEquals("Empty url" , result);
	}
	
	@Test
	public void invalidUrl()
	  throws Exception {
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setJavaScriptEnabled(false);
	    String baseUrl = "mmm";
	  
	    String result = SainsburyScraper.run(baseUrl);
	    Assert.assertEquals("Invalid url" , result);
	}
	
	@Test
	public void validUrlButNoProducts()
	  throws Exception {
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setJavaScriptEnabled(false);
	    String baseUrl = "http://www.google.com";
	  
	    String result = SainsburyScraper.run(baseUrl);
	    Assert.assertEquals("No products found !" , result);
	}
	
	@Test
	public void validUrlBut404()
	  throws Exception {
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setJavaScriptEnabled(false);
	    String baseUrl = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.htmlzz";
	  
	    String result = SainsburyScraper.run(baseUrl);
	    Assert.assertEquals("Error scenario" , result);
	}
	
	@Test
	public void success()
	  throws Exception {
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setJavaScriptEnabled(false);
	    String baseUrl = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
	  
	    String result = SainsburyScraper.run(baseUrl);
	    Assert.assertEquals("success" , result);
	}

}
