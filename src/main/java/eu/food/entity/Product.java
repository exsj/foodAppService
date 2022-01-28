package eu.food.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	private String name;
	private String description;
	private int price;
	@ManyToOne
    @JoinColumn(name="vendor_id", nullable=false)
	private Vendor vendor;
	private Long vendorProductId;
	@OneToMany(mappedBy="product")
	private List<OrderProduct> orderProductList;
	
}
