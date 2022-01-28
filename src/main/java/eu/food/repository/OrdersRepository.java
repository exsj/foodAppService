package eu.food.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import eu.food.entity.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>{

	List<Orders> findByUserId(Long userId);

	@Query
	List<Orders> getOrdersByUserId(Long userId);
	
}
