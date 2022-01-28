package eu.food.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Vendor {

	@Id
	private Long vendorId;
	
	private String name;
	
	private String description;
	
	@OneToMany(mappedBy="vendor")
	private Set<Product> listProducts;
	
}
