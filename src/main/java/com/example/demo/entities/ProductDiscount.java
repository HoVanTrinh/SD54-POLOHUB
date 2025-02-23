package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

@Entity
@Table(name = "product_discount")
public class ProductDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double discountedAmount;
    private Date startDate;
    private Date endDate;
    private boolean closed;

    @OneToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
}
