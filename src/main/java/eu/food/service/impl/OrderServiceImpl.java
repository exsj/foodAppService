package eu.food.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import eu.food.dto.OrderDto;
import eu.food.dto.OrderDtoRequest;
import eu.food.dto.OrderItemDto;
import eu.food.entity.OrderProduct;
import eu.food.entity.Orders;
import eu.food.entity.Product;
import eu.food.feign.BankClient;
import eu.food.model.Constants;
import eu.food.model.ResponseBody;
import eu.food.model.Transaction;
import eu.food.repository.OrderProductRepository;
import eu.food.repository.OrdersRepository;
import eu.food.repository.ProductRepository;
import eu.food.service.OrderService;
import eu.food.utils.Utils;
import feign.FeignException.FeignClientException;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrdersRepository ordersRepository;
	
	@Autowired
	OrderProductRepository orderProductRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	BankClient bankClient;
	
	@Value("${app.food.accountId}")
	private String appAccountId;

	private ResponseEntity<ResponseBody> createHistory(ResponseBody responseBody, List<Orders> listOrders) {
		List<OrderDto> listOrdersDto = new ArrayList<>();
		listOrders.forEach(elem -> {
			
			OrderDto orderDto = new OrderDto();
			BeanUtils.copyProperties(elem, orderDto);
			
			List<OrderItemDto> orderItemDtoList = new ArrayList<>();
			elem.getListProducts().forEach(elem2 -> {
				OrderItemDto orderItemDto = new OrderItemDto();
				BeanUtils.copyProperties(elem2, orderItemDto);
				orderItemDto.setName(elem2.getProduct().getName());
				orderItemDto.setVendorName(elem2.getProduct().getVendor().getName());
				orderItemDtoList.add(orderItemDto);
			});
			
			orderDto.setListProducts(orderItemDtoList);
			listOrdersDto.add(orderDto);
		});
		
		if (listOrdersDto.isEmpty()) {
			responseBody.setMessage(Constants.NO_ORDERS);
		} else {
			responseBody.setOrderList(listOrdersDto);
			responseBody.setTotalRecords(listOrdersDto.size());
		}
		
		return Utils.makeStatus(responseBody, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseBody> history(String userId) {
		ResponseBody responseBody = new ResponseBody();
		List<Orders> listOrders = null;
		if (userId == null) {
			listOrders = ordersRepository.findAll();
		} else {
			Long id = 0l;
			try {
				id = Long.valueOf(userId);
			} catch(Exception e) {
				responseBody.setMessage(Constants.USER_IS_INVALID);
				return Utils.makeStatus(responseBody, HttpStatus.BAD_REQUEST);
			}
			if (id < 1l){
				responseBody.setMessage(Constants.USER_INVALID);
				return Utils.makeStatus(responseBody, HttpStatus.BAD_REQUEST);
			} else {
				listOrders = ordersRepository.findByUserId(id);
			}
		}  
		
		return createHistory(responseBody, listOrders);
	}

	@Override
	public ResponseEntity<ResponseBody> buy(OrderDtoRequest orderDtoRequest) {
		ResponseBody responseBody = new ResponseBody();
		
		Orders order = new Orders();
		order.setUserId(orderDtoRequest.getUserId());
		order.setOrderDate(LocalDateTime.now());
		List<OrderProduct> orderProductList = new ArrayList<>();
		
		orderDtoRequest.getProductList().forEach(elem -> {
			Optional<Product> productDb = productRepository.findById(elem.getProductId());
			
			OrderProduct orderProductAux = new OrderProduct();
			orderProductAux.setProduct(productDb.get());
			orderProductAux.setQuantity(elem.getQuantity());
			
			order.setPrice(order.getPrice() + (productDb.get().getPrice() * elem.getQuantity()));
			
			orderProductList.add(orderProductAux);
		});
		
		Transaction transaction = new Transaction();
		transaction.setUserId(orderDtoRequest.getAccountNo());
		transaction.setToAcc(Long.parseLong(appAccountId));
		transaction.setAmount(order.getPrice());
		transaction.setComments("Food order");
		try {
			bankClient.transfer(transaction);
		} catch (FeignClientException e) {
			responseBody.setMessage("Cannot process buy order, balance is lower than total price to pay");
			return Utils.makeStatus(responseBody, HttpStatus.BAD_REQUEST);
		}
		

		Orders orderDb = ordersRepository.save(order);
		
		orderProductList.forEach(elem -> {
			
			elem.setOrder(orderDb);
			orderProductRepository.save(elem);
		});
		
		responseBody.setMessage(Constants.ORDER_SUCCES);
		return Utils.makeStatus(responseBody, HttpStatus.OK);
		
	}
	
}
