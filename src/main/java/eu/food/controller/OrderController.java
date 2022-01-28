package eu.food.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.food.dto.OrderDtoRequest;
import eu.food.model.ResponseBody;
import eu.food.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/history")
	public ResponseEntity<ResponseBody> history() {
		return this.history(null);
	}
	
	@GetMapping("/history/{userId}")
	public ResponseEntity<ResponseBody> history(@PathVariable String userId) {
		return orderService.history(userId);
	}
	
	@PostMapping("/buy")
	public ResponseEntity<ResponseBody> buy(@Valid @RequestBody OrderDtoRequest orderDto) {
		return orderService.buy(orderDto);
	}
}
