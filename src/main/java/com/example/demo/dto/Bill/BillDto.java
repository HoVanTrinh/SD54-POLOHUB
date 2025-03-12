package com.example.demo.dto.Bill;

import com.example.demo.dto.CustomerDto.CustomerDto;
import com.example.demo.entities.enumClass.BillStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillDto {
    private Long id;
    private String code;
    private double promotionPrice;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private BillStatus status;
    private Boolean returnStatus;
    private CustomerDto customer;
    private Double totalAmount;
    private Voucher voucher;

    @Data
    public class Voucher {
        private Long id;
        private Double minimumValueForDiscount;
        private Double billDiscount;
    }
}
