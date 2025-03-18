package com.example.demo.repositories;

import com.example.demo.dto.Statistic.BestSellerProduct;
import com.example.demo.dto.Statistic.ProductStatistic;
import com.example.demo.dto.product.ProductSearchDto;
import com.example.demo.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Product findByCode(String code);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> getAllByName(String name, Pageable pageable);

    boolean existsByCode(String code);
    boolean existsByName(String name);
    Page<Product> findAllByStatus(int status, Pageable pageable);
    Page<Product> findAllByStatusAndDeleteFlag(int status, boolean deleteFlag, Pageable pageable);

    @Query(value = "SELECT p.id as id, " +
            "p.code as code, " +
            "p.name as name, " +
            "br.name as brandName, " +
            "mt.name as materialName, " +
            "p.price as price " +
            "FROM Product p " +
            "INNER JOIN Brand br ON br.id = p.brand.id " +
            "INNER JOIN Material mt ON mt.id = p.material.id " +
            "WHERE (:productName IS NULL OR p.name LIKE %:productName%) " +
            "AND p.deleteFlag = false")
    Page<Product> searchProductName(String productName, Pageable pageable);

//    @Query(value = "SELECT p.id as idSanPham, p.code as maSanPham, p.name as tenSanPham, " +
//            "p.brand.name as nhanHang, p.material.name as chatLieu ,p.status as trangThai, p.productDetails as quantity " +
//            "FROM Product p " +
//            "WHERE (COALESCE(:maSanPham, '') = '' OR p.code LIKE CONCAT('%', :maSanPham, '%')) " +
//            "AND (COALESCE(:tenSanPham, '') = '' OR p.name LIKE CONCAT('%', :tenSanPham, '%')) " +
//            "AND (:nhanHang IS NULL OR p.brand.id = :nhanHang) " +
//            "AND (:chatLieu IS NULL OR p.material.id = :chatLieu) " +
//            "AND (:trangThai IS NULL OR p.status = :trangThai) " +
//            "AND (:quantity IS NULL OR p.productDetails = :quantity)" +
//            "AND p.deleteFlag = false")
//    Page<ProductSearchDto> listSearchProduct(String maSanPham, String tenSanPham, Long nhanHang, Long chatLieu, Integer trangThai, Pageable pageable);
//@Query(value = "SELECT p.id as idSanPham, p.code as maSanPham, p.name as tenSanPham, " +
//        "p.brand.name as nhanHang, p.material.name as chatLieu, p.status as trangThai, " +
//        "SUM(pd.quantity) as quantity " + // Lấy số lượng từ ProductDetail
//        "FROM Product p " +
//        "JOIN p.productDetails pd " + // Kết nối với ProductDetail
//        "WHERE (COALESCE(:maSanPham, '') = '' OR p.code LIKE CONCAT('%', :maSanPham, '%')) " + // Sửa dấu nháy
//        "AND (COALESCE(:tenSanPham, '') = '' OR p.name LIKE CONCAT('%', :tenSanPham, '%')) " +
//        "AND (:nhanHang IS NULL OR p.brand.id = :nhanHang) " +
//        "AND (:chatLieu IS NULL OR p.material.id = :chatLieu) " +
//        "AND (:trangThai IS NULL OR p.status = :trangThai) " +
//        "AND (:quantity IS NULL OR pd.quantity = :quantity) " + // Kiểm tra quantity
//        "AND p.deleteFlag = false")
//Page<ProductSearchDto> listSearchProduct(String maSanPham, String tenSanPham, Long nhanHang, Long chatLieu, Integer trangThai, Integer quantity, Pageable pageable);
@Query(value = "SELECT p.id AS idSanPham, p.code AS maSanPham, p.name AS tenSanPham, " +
        "p.brand.name AS nhanHang, p.material.name AS chatLieu, p.status AS trangThai, " +
        "SUM(pd.quantity) AS quantity " +
        "FROM Product p " +
        "JOIN p.productDetails pd " +
        "WHERE (COALESCE(:maSanPham, '') = '' OR p.code LIKE CONCAT('%', :maSanPham, '%')) " +
        "AND (COALESCE(:tenSanPham, '') = '' OR p.name LIKE CONCAT('%', :tenSanPham, '%')) " +
        "AND (:nhanHang IS NULL OR p.brand.id = :nhanHang) " +
        "AND (:chatLieu IS NULL OR p.material.id = :chatLieu) " +
        "AND (:trangThai IS NULL OR p.status = :trangThai) " +
        "AND (:quantity IS NULL OR pd.quantity = :quantity) " +
        "AND p.deleteFlag = false " +
        "GROUP BY p.id, p.code, p.name, p.brand.name, p.material.name, p.status, p.createDate")
Page<ProductSearchDto> listSearchProduct(String maSanPham, String tenSanPham, Long nhanHang,
                                         Long chatLieu, Integer trangThai,
                                         Integer quantity, Pageable pageable);
    @Query(value = "SELECT p.id as idSanPham,p.code as maSanPham,p.name as tenSanPham,p.brand.name as nhanHang,p.material.name as chatLieu,p.status as trangThai FROM Product p where p.deleteFlag = false")
    Page<ProductSearchDto> getAll(Pageable pageable);

    Page<Product> findAllByDeleteFlagFalse(Pageable pageable);

    @Query("select p from Product p join ProductDetail pd on p.id = pd.product.id where pd.id = :productDetaiLId")
    Product findByProductDetail_Id(Long productDetaiLId);

    Product findTopByOrderByIdDesc();





}
