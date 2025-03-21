package com.example.demo.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String imageUrl;
    private Double priceMin;

    private List<ProductDetailDto> productDetailDtos;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
    private boolean isDiscounted;
    private Long totalSold;

    public Long getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(Long totalSold) {
        this.totalSold = totalSold;
    }
}
