package eu.food.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eu.food.dto.OrderDto;
import eu.food.dto.ProductDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(Include.NON_EMPTY)
public class ResponseBody {
	
	private String message;
	private Integer totalRecords;
	private List<ProductDto> listOfProducts;
	private List<OrderDto> orderList;
	
}
