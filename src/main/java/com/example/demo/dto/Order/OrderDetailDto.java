package com.example.demo.dto.Order;

import lombok.Data;

@Data
public class OrderDetailDto {
    private Integer quantity;
    private Long productDetailId;
}
