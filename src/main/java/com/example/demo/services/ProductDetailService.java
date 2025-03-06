package com.example.demo.services;

import com.example.demo.dto.product.ProductDetailDto;
import com.example.demo.entities.ProductDetail;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductDetailService {
    ProductDetail save(ProductDetail productDetail);

    ProductDetail getProductDetailByProductCode(String code) throws NotFoundException;

    List<ProductDetailDto> getByProductId(Long id) throws com.example.demo.exceptions.NotFoundException;
}
