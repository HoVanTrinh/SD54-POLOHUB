package com.example.demo.dto.ProductDiscount;

import lombok.Data;

import java.util.List;

@Data
public class ProductDiscountCreateDto {
    List<ProductDiscountDto> productDiscounts;
}
