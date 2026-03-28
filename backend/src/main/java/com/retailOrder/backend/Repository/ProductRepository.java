package com.retailOrder.backend.Repository;



import com.retailOrder.backend.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
