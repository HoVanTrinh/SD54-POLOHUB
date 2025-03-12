package com.example.demo.dto.Bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InStoreInvoiceDto {
    private Long id;
    public InStoreInvoiceDetail inStoreInvoiceDetail;
}
