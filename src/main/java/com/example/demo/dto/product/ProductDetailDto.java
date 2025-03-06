package com.example.demo.dto.product;

import com.example.demo.entities.Color;
import com.example.demo.entities.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDto {
    private Long id;
    private int quantity;
    private String barcode;
    private double price;
    private int status;
    private Long productId;
    private Size size;
    private Color color;
    private Double discountedPrice;
}
