package eu.food.service;

import org.springframework.http.ResponseEntity;

import eu.food.dto.OrderDtoRequest;
import eu.food.model.ResponseBody;

public interface OrderService {

	ResponseEntity<ResponseBody> history(String userId);

	ResponseEntity<ResponseBody> buy(OrderDtoRequest orderDto);

}
