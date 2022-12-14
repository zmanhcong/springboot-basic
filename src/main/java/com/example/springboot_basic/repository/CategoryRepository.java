package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM categories c WHERE c.category_name LIKE %:keyword%", nativeQuery = true)
    List<Category> findAllNative(String keyword);

    List<Category> findByNameContaining(String name);

    Page<Category> findByNameContaining (String name, Pageable pageable);
}
