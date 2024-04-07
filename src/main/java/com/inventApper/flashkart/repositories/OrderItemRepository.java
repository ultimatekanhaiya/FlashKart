package com.inventApper.flashkart.repositories;

import com.inventApper.flashkart.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
