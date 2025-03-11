package com.example.demo.dto.Cart;

import com.example.demo.dto.product.ProductDetailDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;
    private Long accountId;
    private ProductCart product;
    private ProductDetailDto detail;

    @NotNull
    private int quantity;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
