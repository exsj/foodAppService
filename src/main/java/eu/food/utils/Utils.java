package eu.food.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import eu.food.model.ResponseBody;

public class Utils {
	
	private Utils() {}
	
	public static ResponseEntity<ResponseBody> makeStatus(ResponseBody responseBody, HttpStatus status) {
		return new ResponseEntity<>(responseBody, status);
	}

}
