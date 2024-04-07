package com.inventApper.flashkart.repositories;

import com.inventApper.flashkart.entities.Order;
import com.inventApper.flashkart.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);
}
