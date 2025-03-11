package com.example.demo.services.serviceImpl;

import com.example.demo.dto.AddressShipping.AddressShippingDto;
import com.example.demo.dto.AddressShipping.AddressShippingDtoAdmin;
import com.example.demo.entities.Account;
import com.example.demo.entities.AddressShipping;
import com.example.demo.entities.Customer;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.ShopApiException;
import com.example.demo.repositories.AddressShippingRepository;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.sercurity.CustomUserDetails;
import com.example.demo.services.AddressShippingService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressShippingServiceImpl implements AddressShippingService {
    private final AddressShippingRepository addressShippingRepository;
    private final CustomerRepository customerRepository;

    public AddressShippingServiceImpl(AddressShippingRepository addressShippingRepository, CustomerRepository customerRepository) {
        this.addressShippingRepository = addressShippingRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<AddressShippingDto> getAddressShippingByAccountId() {
        List<AddressShipping> addressShippings = addressShippingRepository.findAllByCustomer_Account_Id(getCurrentLogin().getId());
        List<AddressShippingDto> addressShippingDtos = new ArrayList<>();
        addressShippings.forEach(item -> {
            AddressShippingDto addressShippingDto = new AddressShippingDto();
            addressShippingDto.setId(item.getId());
            addressShippingDto.setAddress(item.getAddress());
            addressShippingDtos.add(addressShippingDto);
        });
        return addressShippingDtos;
    }

    @Override
    public AddressShippingDto saveAddressShippingUser(AddressShippingDto addressShippingDto) {
        List<AddressShipping> addressShippings = addressShippingRepository.findAllByCustomer_Account_Id(getCurrentLogin().getId());
        if(addressShippings.size() > 5) {
            throw new ShopApiException(HttpStatus.BAD_REQUEST, "Bạn chỉ được thêm tối đa 5 địa chỉ");
        }
        AddressShipping addressShipping = new AddressShipping();
        addressShipping.setAddress(addressShippingDto.getAddress());
        Customer customer = new Customer();
        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            customer = getCurrentLogin().getCustomer();
            addressShipping.setCustomer(customer);
        }

        AddressShipping addressShippingNew = addressShippingRepository.save(addressShipping);
        return new AddressShippingDto(addressShippingNew.getId(), addressShippingNew.getAddress());
    }

    @Override
    public AddressShippingDto saveAddressShippingAdmin(AddressShippingDtoAdmin addressShippingDto) {
        AddressShipping addressShipping = new AddressShipping();
        addressShipping.setAddress(addressShipping.getAddress());
        Customer customer = customerRepository.findById(addressShippingDto.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        addressShipping.setCustomer(customer);

        AddressShipping addressShippingNew = addressShippingRepository.save(addressShipping);
        return new AddressShippingDto(addressShippingNew.getId(), addressShippingNew.getAddress());
    }

    @Override
    public void deleteAddressShipping(Long id) {
        AddressShipping addressShipping = addressShippingRepository.findById(id).orElseThrow(null);
        addressShippingRepository.delete(addressShipping);
    }

    private Account getCurrentLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return customUserDetails.getAccount();
        }

        // Handle the case where the principal is not a CustomUserDetails
        return null; // or throw an exception, depending on your use case
    }
}
