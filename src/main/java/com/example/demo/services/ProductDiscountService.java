package com.example.demo.services;

import com.example.demo.dto.ProductDiscount.ProductDiscountCreateDto;
import com.example.demo.dto.ProductDiscount.ProductDiscountDto;
import com.example.demo.entities.Color;
import com.example.demo.entities.ProductDiscount;
import com.example.demo.entities.Size;

import java.util.List;

public interface ProductDiscountService {
    // Lấy tất cả các giảm giá sản phẩm
    List<ProductDiscount> getAllProductDiscount();

    // Cập nhật trạng thái đóng/mở của giảm giá sản phẩm
    ProductDiscountDto updateCloseProductDiscount(Long discountId, boolean closed);

    // Tạo nhiều giảm giá sản phẩm
    List<ProductDiscountDto> createProductDiscountMultiple(ProductDiscountCreateDto productDiscountCreateDto);

    // Lấy tất cả các giảm giá sản phẩm với bộ lọc màu sắc và kích cỡ
    List<ProductDiscount> getAllProductDiscount(Color color, Size size); // Đổi từ String thành Color và Size

    // Lấy giảm giá theo màu sắc
    List<ProductDiscount> getDiscountsByColor(Color color); // Đổi từ String thành Color

    // Lấy giảm giá theo kích cỡ
    List<ProductDiscount> getDiscountsBySize(Size size); // Đổi từ String thành Size
}
