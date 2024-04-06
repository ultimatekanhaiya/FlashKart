package com.inventApper.flashkart.repositories;

import com.inventApper.flashkart.entities.Cart;
import com.inventApper.flashkart.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByUser(User user);
}
