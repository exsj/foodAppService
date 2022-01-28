package eu.food.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDto {
	private Long productId;
	private String name;
	private String description;
	private String vendorName;
	private int price;

}
