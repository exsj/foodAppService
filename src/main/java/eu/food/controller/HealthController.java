package eu.food.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class HealthController {

	@GetMapping("/ping")
	public String getHealth() {
		return "pong";
	}
	
}
