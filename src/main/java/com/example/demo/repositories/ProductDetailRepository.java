package com.example.demo.repositories;

import com.example.demo.entities.Product;
import com.example.demo.entities.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    Page<ProductDetail> getProductDetailsByProductId(Long id, Pageable pageable);

    ProductDetail getProductDetailByProduct(Product product);
    List<ProductDetail> getProductDetailByProductId(Long productId);

    ProductDetail findByBarcodeContainingIgnoreCase(String barcode);

    boolean existsByBarcode(String barcode);
}
