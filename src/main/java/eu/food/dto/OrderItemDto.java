package eu.food.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemDto {
	
	private String name;
	private Integer quantity;
	private String vendorName;

}
