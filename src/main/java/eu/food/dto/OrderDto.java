package eu.food.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDto {
	
	private Long userId;
	private LocalDateTime orderDate;
	private int price;
	private List<OrderItemDto> listProducts;

}
