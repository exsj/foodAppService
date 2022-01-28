package eu.food.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import eu.food.dto.ProductDto;
import eu.food.entity.Product;
import eu.food.model.Constants;
import eu.food.model.ResponseBody;
import eu.food.repository.ProductRepository;
import eu.food.service.ProductService;
import eu.food.utils.Utils;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Override
	public ResponseEntity<ResponseBody> search(String textInput) {
		ResponseBody responseBody = new ResponseBody();
		
		if (StringUtils.isBlank(textInput)) {
			responseBody.setMessage(Constants.TEXT_INPUT_NULL);
			return Utils.makeStatus(responseBody, HttpStatus.BAD_REQUEST);
		}
		
		List<Product> productList = productRepository.getAllProductsSearch(textInput);
		List<ProductDto> productDtoList = new ArrayList<>();
		
		productList.forEach(elem -> {
			ProductDto productDto = new ProductDto();
			BeanUtils.copyProperties(elem, productDto);
			productDto.setVendorName(elem.getVendor().getName());
			productDtoList.add(productDto);
		});
		
		if (productList.isEmpty()) {
			responseBody.setMessage(Constants.NO_PRODUCTS);
		} else {
			responseBody.setListOfProducts(productDtoList);
			responseBody.setTotalRecords(productDtoList.size());
		}
		
		return Utils.makeStatus(responseBody, HttpStatus.OK);
	}

}
