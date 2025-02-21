package com.example.demo.services;

import com.example.demo.dto.AddressShipping.AddressShippingDto;
import com.example.demo.dto.AddressShipping.AddressShippingDtoAdmin;

import java.util.List;

public interface AddressShippingService {
    List<AddressShippingDto> getAddressShippingByAccountId();
    AddressShippingDto saveAddressShippingUser(AddressShippingDto addressShippingDto);

    AddressShippingDto saveAddressShippingAdmin(AddressShippingDtoAdmin addressShippingDto);

    void deleteAddressShipping(Long id);
}
