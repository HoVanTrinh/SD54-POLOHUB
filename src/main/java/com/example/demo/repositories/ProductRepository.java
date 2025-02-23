package com.example.demo.repositories;

import com.example.demo.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Product findByCode(String code);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> getAllByName(String name, Pageable pageable);

    boolean existsByCode(String code);

    Page<Product> findAllByStatus(int status, Pageable pageable);
    Page<Product> findAllByStatusAndDeleteFlag(int status, boolean deleteFlag, Pageable pageable);




}
