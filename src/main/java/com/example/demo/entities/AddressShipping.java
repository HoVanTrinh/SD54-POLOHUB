package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "AddressShipping")
public class AddressShipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private int provinceId;
//    private int districtId;
//    private int wardId;
//
//
//    private String street;


    @Nationalized
    @Column(nullable = false, length = 150)
    private String address;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    Customer customer;
}
