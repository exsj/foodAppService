package eu.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.food.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

}
