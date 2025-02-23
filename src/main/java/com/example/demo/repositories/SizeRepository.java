package com.example.demo.repositories;

import com.example.demo.entities.Color;
import com.example.demo.entities.Product;
import com.example.demo.entities.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, Long> {
    @Query(value = "select distinct s from Size s join ProductDetail pd on s.id = pd.size.id where pd.product = :product")
    List<Size> findSizesByProduct(Product product);

    @Query(value = "select distinct s from Size s join ProductDetail pd on s.id = pd.size.id where pd.product = :product and pd.color = :color")
    List<Size> findSizesByProductAndColor(Product product, Color color);

    boolean existsByCode(String code);

    List<Size> findAllByDeleteFlagFalse();
    Page<Size> findAllByDeleteFlagFalse(Pageable pageable);

    boolean existsByCodeAndDeleteFlagFalse(String code);

    Size findByCodeAndDeleteFlagTrue(String code);
    // Thêm phương thức để tìm đối tượng Size theo tên
    Optional<Size> findByName(String name);
}
