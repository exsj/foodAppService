package eu.food.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import eu.food.dto.OrderDtoRequest;
import eu.food.dto.OrderItemDtoRequest;
import eu.food.entity.OrderProduct;
import eu.food.entity.Orders;
import eu.food.entity.Product;
import eu.food.entity.Vendor;
import eu.food.feign.BankClient;
import eu.food.model.Constants;
import eu.food.model.ResponseBody;
import eu.food.model.Transaction;
import eu.food.repository.OrderProductRepository;
import eu.food.repository.OrdersRepository;
import eu.food.repository.ProductRepository;
import eu.food.service.impl.OrderServiceImpl;
import eu.food.utils.Utils;

@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {
	
	@InjectMocks
    OrderServiceImpl orderServiceImpl;
	
	@Mock
	OrdersRepository ordersRepository;
	
	@Mock
	OrderProductRepository orderProductRepository;
	
	@Mock
	ProductRepository productRepository;
	
	@Mock
	BankClient bankClient;
	
	@BeforeEach
	public void init() {
		ReflectionTestUtils.setField(orderServiceImpl, "appAccountId", "1");
	}
	
	@Test
    public void testOrderHistoryAll(){
		Orders order = new Orders();
		Set<OrderProduct> listProducts = new HashSet<>();
		OrderProduct orderProduct = new OrderProduct();
		Product product = new Product();
		product.setName("testProductName");
		Vendor vendor = new Vendor();
		vendor.setName("testVendorName");
		product.setVendor(vendor);
		orderProduct.setProduct(product);
		listProducts.add(orderProduct);
		order.setListProducts(listProducts);
		List<Orders> listOrders = new ArrayList<>();
		listOrders.add(order);
		
		when(ordersRepository.findAll()).thenReturn(listOrders);
		ResponseEntity<ResponseBody> response = orderServiceImpl.history(null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getOrderList().size());
        assertEquals(1, response.getBody().getTotalRecords());
    }
	
    public void testOrderHistoryByUserId(){
		Orders order = new Orders();
		Set<OrderProduct> listProducts = new HashSet<>();
		order.setListProducts(listProducts);
		String userId = "1";
		Long userIdLong = 1l;
		List<Orders> listOrders = new ArrayList<>();
		listOrders.add(order);
		
		when(ordersRepository.findByUserId(userIdLong)).thenReturn(listOrders);
		ResponseEntity<ResponseBody> response = orderServiceImpl.history(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getOrderList().size());
        assertEquals(1, response.getBody().getTotalRecords());
    }
	
	@Test
    public void testOrderHistoryAllNoOrders(){
		List<Orders> listOrders = new ArrayList<>();
		
		when(ordersRepository.findAll()).thenReturn(listOrders);
		ResponseEntity<ResponseBody> response = orderServiceImpl.history(null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Constants.NO_ORDERS, response.getBody().getMessage());
    }
	
	@Test
    public void testOrderHistoryByUserIdNoOrders(){
		String userId = "1";
		
		ResponseEntity<ResponseBody> response = orderServiceImpl.history(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Constants.NO_ORDERS, response.getBody().getMessage());
    }
	
	@Test
    public void testOrderHistoryByUserIdLowerThanOneUserId(){
		String userId = "0";
		
		ResponseEntity<ResponseBody> response = orderServiceImpl.history(userId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.USER_INVALID, response.getBody().getMessage());
    }
	
	@Test
    public void testOrderHistoryByUserIdWrongUserId(){
		String userId = "1.5";
		
		ResponseEntity<ResponseBody> response = orderServiceImpl.history(userId);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.USER_IS_INVALID, response.getBody().getMessage());
    }
	
	@Test
	public void testBuy() {
		Transaction transaction = new Transaction();
		ResponseEntity<ResponseBody> responseBank = Utils.makeStatus(null, HttpStatus.OK);
		Product product = new Product();
		product.setPrice(1);
		Optional<Product> productOptional = Optional.of(product);
		OrderItemDtoRequest orderItemDtoRequest = new OrderItemDtoRequest();
		orderItemDtoRequest.setProductId(1l);
		orderItemDtoRequest.setQuantity(1);
		List<OrderItemDtoRequest> orderItemDtoRequestList = new ArrayList<>();
		orderItemDtoRequestList.add(orderItemDtoRequest);
		OrderDtoRequest orderDtoRequest = new OrderDtoRequest();
		orderDtoRequest.setUserId(1l);
		orderDtoRequest.setProductList(orderItemDtoRequestList);
		Orders order = new Orders();
		Orders orderDb = new Orders();
		when(productRepository.findById(1l)).thenReturn(productOptional);
		when(bankClient.transfer(transaction)).thenReturn(responseBank);
		when(ordersRepository.save(order)).thenReturn(orderDb);
		
		ResponseEntity<ResponseBody> response = orderServiceImpl.buy(orderDtoRequest);
		assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Constants.ORDER_SUCCES, response.getBody().getMessage());
		
	}
	
}
