package com.retailOrder.backend.Repository;

import com.retailOrder.backend.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}