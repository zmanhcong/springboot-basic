package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends CrudRepository<Category, Long> {
    Page<Category> findAll(Pageable pageable);

    <S extends Category> S save(S entity);

    <S extends Category> Iterable<S> saveAll(Iterable<S> entities);

    Optional<Category> findById(Long aLong);

    boolean existsById(Long aLong);


    Page<Category> findByNameContaining(String name, Pageable pageable);

    @Query(value = "SELECT * FROM categories c WHERE c.category_name LIKE %:keyword%", nativeQuery = true)
    List<Category> findAllNative(String keyword);

    List<Category> findByNameContaining(String name);

    Iterable<Category> findAll();

    Iterable<Category> findAllById(Iterable<Long> longs);

    long count();

    void deleteById(Long aLong);

    void delete(Category entity);

    void deleteAllById(Iterable<? extends Long> longs);


}
