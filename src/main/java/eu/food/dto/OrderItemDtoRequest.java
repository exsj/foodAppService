package eu.food.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemDtoRequest {

	@NotNull
	@Min(1)
	private Long productId;
	
	@NotNull
	@Min(1)
	private Integer quantity;
	
}
