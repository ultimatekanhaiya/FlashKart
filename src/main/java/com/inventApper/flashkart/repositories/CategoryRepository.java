package com.inventApper.flashkart.repositories;

import com.inventApper.flashkart.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
