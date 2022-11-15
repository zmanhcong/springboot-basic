package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryService extends CrudRepository<Category, Long> {
    <S extends Category> S save(S entity);

    <S extends Category> Iterable<S> saveAll(Iterable<S> entities);

    Optional<Category> findById(Long aLong);

    boolean existsById(Long aLong);

    Iterable<Category> findAll();

    Iterable<Category> findAllById(Iterable<Long> longs);

    long count();

    void deleteById(Long aLong);

    void delete(Category entity);

    void deleteAllById(Iterable<? extends Long> longs);

    void deleteAll(Iterable<? extends Category> entities);

    void deleteAll();
}
