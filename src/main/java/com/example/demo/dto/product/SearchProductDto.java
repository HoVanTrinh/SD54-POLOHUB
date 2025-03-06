package com.example.demo.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductDto {
    private String code;
    private Double minPrice;
    private Double maxPrice;
    private String productName;
    private List<Long> categoryId;
    private List<Long> brandId;
    private String keyword;
    private String barcode;
    private String sort;
    private String gender;
}
