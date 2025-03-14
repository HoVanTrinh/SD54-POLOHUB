package com.example.demo.dto.Account;
import com.example.demo.dto.AddressShipping.AddressShippingDto;
import lombok.Data;

import java.util.List;
@Data
public class AccountDto {
    private String phoneNumber;
    private String name;
    private String email;
    private String password;
    private List<AddressShippingDto> addressShippingList;
}
