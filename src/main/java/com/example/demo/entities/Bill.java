package com.example.demo.entities;

import com.example.demo.entities.enumClass.BillStatus;
import com.example.demo.entities.enumClass.InvoiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "Bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String code;

    private double promotionPrice;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type")
    private InvoiceType invoiceType;

    @Nationalized
    private String billingAddress;

    private Double amount;

    private Boolean returnStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
    private List<BillDetail> billDetail;

    @ManyToOne
    @JoinColumn(name = "discount_code_id")
    private DiscountCode discountCode;
}
