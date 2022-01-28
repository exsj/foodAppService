package eu.food.dto;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDtoRequest {
	
	@NotNull
	@Min(1)
	private Long userId;
	
	@NotNull
	@Min(1)
	private Long accountNo;
	
	@NotNull
	@Size(min=1)
	private List<OrderItemDtoRequest> productList;

}
