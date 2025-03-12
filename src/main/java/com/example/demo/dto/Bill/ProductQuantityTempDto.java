package com.example.demo.dto.Bill;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ProductQuantityTempDto {
    private Long createId;
    private List<ProductDetail> productDetail;

    @Getter
    @Setter
    public static class ProductDetail {
        private Long productDetailId;
        private Integer quantity;
    }
}
