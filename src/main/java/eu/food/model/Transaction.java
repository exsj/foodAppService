package eu.food.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Transaction {
	
	private Long userId;
	
	private Long toAcc;
	
	private Integer amount;
	
	private String comments;
	
}
