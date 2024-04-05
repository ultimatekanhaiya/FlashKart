package com.inventApper.flashkart.repositories;

import com.inventApper.flashkart.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

    // search
    Page<Product> findByTitleContaining(String title, Pageable pageable);

    Page<Product> findByLiveTrue(Pageable pageable);

}
