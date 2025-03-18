package com.example.demo.services;

import com.example.demo.dto.product.ProductDto;
import com.example.demo.dto.product.ProductSearchDto;
import com.example.demo.dto.product.SearchProductDto;
import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Page<Product> getAllProduct(Pageable pageable0);

    Page<ProductSearchDto> getAll(Pageable pageable);

    Product save(Product product) throws IOException;

    Product add(Product product) throws IOException;

    Product delete(Long id);

    boolean existsByName(String name);
    Product getProductByCode(String code);

    boolean existsByCode(String code);

    Page<Product> search(String productName, Pageable pageable);



    Page<ProductSearchDto> listSearchProduct(String maSanPham, String tenSanPham, Long nhanHang, Long chatLieu, Integer trangThai,Integer quantity,Pageable pageable);

    Page<Product> getAllByStatus(int status, Pageable pageable);

    Optional<Product> getProductById(Long id);

    Page<ProductDto> searchProduct(SearchProductDto searchDto, Pageable pageable);

    Page<ProductDto> getAllProductApi(Pageable pageable);

    ProductDto getProductByBarcode(String barcode);

    List<ProductDto> getAllProductNoPaginationApi(SearchProductDto searchRequest);

    ProductDto getByProductDetailId(Long detailId);



}
