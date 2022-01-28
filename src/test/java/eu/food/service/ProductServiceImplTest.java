package eu.food.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import eu.food.entity.Product;
import eu.food.entity.Vendor;
import eu.food.model.Constants;
import eu.food.model.ResponseBody;
import eu.food.repository.ProductRepository;
import eu.food.service.impl.ProductServiceImpl;

@ExtendWith(SpringExtension.class)
public class ProductServiceImplTest {
	
	@InjectMocks
    ProductServiceImpl productServiceImpl;
	
	@Mock
	ProductRepository productRepository;
	
	@Test
    public void testSearchNullOrBlank(){
		String textInput = "";
		ResponseEntity<ResponseBody> response = productServiceImpl.search(textInput);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(Constants.TEXT_INPUT_NULL, response.getBody().getMessage());
    }
	
	@Test
    public void testSearchNoProducts(){
		String textInput = "burger";
		List<Product> productList = new ArrayList<>();
		
		when(productRepository.getAllProductsSearch(textInput)).thenReturn(productList);
		ResponseEntity<ResponseBody> response = productServiceImpl.search(textInput);
		
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Constants.NO_PRODUCTS, response.getBody().getMessage());
    }
	
	@Test
    public void testSearch(){
		String textInput = "burger";
		Product product = new Product();
		Vendor vendor = new Vendor();
		vendor.setName("testVendorName");
		product.setVendor(vendor);
		List<Product> productList = new ArrayList<>();
		productList.add(product);
		
		when(productRepository.getAllProductsSearch(textInput)).thenReturn(productList);
		ResponseEntity<ResponseBody> response = productServiceImpl.search(textInput);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().getListOfProducts().size());
		assertEquals(1, response.getBody().getTotalRecords());
    }
	
}
