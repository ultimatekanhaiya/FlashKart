package com.inventApper.flashkart.repositories;

import com.inventApper.flashkart.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
