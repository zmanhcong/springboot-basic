package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.Category;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface CategoryService extends CrudRepository<Category, Long> {


    Page<Category> findByNameContaining(String name, Pageable pageable);

    @Query(value = "SELECT * FROM categories c WHERE c.category_name LIKE %:keyword%", nativeQuery = true)
    List<Category> findAllNative(String keyword);

    List<Category> findByNameContaining(String name);

    List<Category> findAll();

    List<Category> findAll(Sort sort);

    List<Category> findAllById(Iterable<Long> longs);

    <S extends Category> List<S> saveAll(Iterable<S> entities);

    void flush();

    <S extends Category> S saveAndFlush(S entity);

    <S extends Category> List<S> saveAllAndFlush(Iterable<S> entities);

    @Deprecated
    void deleteInBatch(Iterable<Category> entities);

    void deleteAllInBatch(Iterable<Category> entities);

    void deleteAllByIdInBatch(Iterable<Long> longs);

    void deleteAllInBatch();

    @Deprecated
    Category getOne(Long aLong);

    @Deprecated
    Category getById(Long aLong);

    Category getReferenceById(Long aLong);

    <S extends Category> List<S> findAll(Example<S> example);

    <S extends Category> List<S> findAll(Example<S> example, Sort sort);

    Page<Category> findAll(Pageable pageable);

    <S extends Category> S save(S entity);

    Optional<Category> findById(Long aLong);

    boolean existsById(Long aLong);

    long count();

    void deleteById(Long aLong);

    void delete(Category entity);

    void deleteAllById(Iterable<? extends Long> longs);

    void deleteAll(Iterable<? extends Category> entities);

    void deleteAll();

    <S extends Category> Optional<S> findOne(Example<S> example);

    <S extends Category> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends Category> long count(Example<S> example);

    <S extends Category> boolean exists(Example<S> example);

    <S extends Category, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}
