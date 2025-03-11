package com.example.demo.services;

import com.example.demo.dto.AddressShipping.AddressShippingDto;
import com.example.demo.dto.AddressShipping.AddressShippingDtoAdmin;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface AddressShippingService {
    List<AddressShippingDto> getAddressShippingByAccountId();
    AddressShippingDto saveAddressShippingUser(AddressShippingDto addressShippingDto);

    AddressShippingDto saveAddressShippingAdmin(AddressShippingDtoAdmin addressShippingDto);

    void deleteAddressShipping(Long id);
}
