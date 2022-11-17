package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM categories c WHERE c.category_name LIKE %:keyword%", nativeQuery = true)
    List<Category> findAllNative(String keyword);

    List<Category> findByNameContaining(String name);
}
