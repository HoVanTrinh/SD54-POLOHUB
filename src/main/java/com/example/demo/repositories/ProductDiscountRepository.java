package com.example.demo.repositories;

import com.example.demo.entities.Color;
import com.example.demo.entities.ProductDetail;
import com.example.demo.entities.ProductDiscount;
import com.example.demo.entities.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
    Page<ProductDiscount> findAllByProductDetailNotNull(Pageable pageable);
    List<ProductDiscount> findAllByProductDetailIn(List<ProductDetail> productDetails);
    ProductDiscount findByProductDetail_Id(Long productDetailId);

    @Query(value = "SELECT * FROM product_discount pd " +
            "WHERE pd.product_detail_id = :productDetailId " +
            "AND GETDATE() BETWEEN pd.start_date AND pd.end_date " +
            "AND pd.closed = 'false'", nativeQuery = true)
    ProductDiscount findValidDiscountByProductDetailId(@Param("productDetailId") Long productDetailId);

    // Thêm phương thức truy vấn dựa trên đối tượng Color
    @Query("SELECT pd FROM ProductDiscount pd WHERE pd.productDetail.color = :color")
    List<ProductDiscount> findByProductDetail_Color(Color color);

    // Thêm phương thức truy vấn dựa trên đối tượng Size
    @Query("SELECT pd FROM ProductDiscount pd WHERE pd.productDetail.size = :size")
    List<ProductDiscount> findByProductDetail_Size(Size size);

    // Thêm phương thức truy vấn dựa trên đối tượng Color và Size
    @Query("SELECT pd FROM ProductDiscount pd WHERE pd.productDetail.color = :color AND pd.productDetail.size = :size")
    List<ProductDiscount> findByProductDetail_ColorAndProductDetail_Size(Color color, Size size);

    @Query("SELECT CASE WHEN COUNT(pd) > 0 THEN true ELSE false END FROM ProductDiscount pd " +
            "WHERE pd.productDetail.id = :productDetailId " +
            "AND pd.closed = false " +
            "AND pd.startDate <= :now AND pd.endDate >= :now")
    boolean existsActiveDiscount(@Param("productDetailId") Long productDetailId, @Param("now") Date now);
}
