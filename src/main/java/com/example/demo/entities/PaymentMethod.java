package com.example.demo.entities;

import com.example.demo.entities.enumClass.PaymentMethodName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "payment_method")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod implements Serializable {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethodName name;
    private int status;
}
