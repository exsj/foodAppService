package eu.food.service;

import org.springframework.http.ResponseEntity;

import eu.food.model.ResponseBody;

public interface ProductService {

	ResponseEntity<ResponseBody> search(String textInput);

}
