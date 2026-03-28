package com.retailOrder.backend.Repository;

import com.retailOrder.backend.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}