package eu.food.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import eu.food.model.Transaction;
import eu.food.model.ResponseBody;

@FeignClient(name = "http://BANK-SERVICE/bank")
public interface BankClient {
	
	@GetMapping("/transfer2")
	public ResponseEntity<ResponseBody> transfer(Transaction transaction);

}
