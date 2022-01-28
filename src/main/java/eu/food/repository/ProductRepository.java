package eu.food.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.food.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("from Product p where p.name LIKE %:textInput%")
	List<Product> getAllProductsSearch(@Param("textInput")String textInput);

}
