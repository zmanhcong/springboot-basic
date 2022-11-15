package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category, Long> {

}
