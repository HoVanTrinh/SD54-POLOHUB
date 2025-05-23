package com.example.demo.services.serviceImpl;

import com.example.demo.dto.CustomerDto.CustomerDto;
import com.example.demo.entities.Customer;
import com.example.demo.exceptions.ShopApiException;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.services.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(this::convertToDto);
    }

    @Override
    public CustomerDto createCustomerAdmin(CustomerDto customerDto) {

        if(customerDto.getCode().trim() == "" || customerDto.getCode() == null) {
            Customer customerCurrent = customerRepository.findTopByOrderByIdDesc();
            Long nextCode = (customerCurrent == null) ? 1 : customerCurrent.getId() + 1;
            String productCode = "KH" + String.format("%04d", nextCode);
            customerDto.setCode(productCode);
        }

        if(customerDto.getCode().trim() != null  ) {
            if(customerRepository.existsByCode(customerDto.getCode())) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã khách hàng đã tồn tại");
            }

        }
        if(customerRepository.existsByPhoneNumber(customerDto.getPhoneNumber())) {
            throw new ShopApiException(HttpStatus.BAD_REQUEST, "Số điện thoại khách hàng đã tồn tại");

        }
        Customer customer = convertToEntity(customerDto);
        return convertToDto(customerRepository.save(customer));
    }

    @Override
    public Page<CustomerDto> searchCustomerAdmin(String keyword, Pageable pageable) {

        //Gọi phương thức từ repository
        Page<Customer> customerPage;
//        Page<Customer> customerPage = customerRepository.searchCustomerKeyword(keyword, pageable);
        try {
            // Kiểm tra xem repository trả về có đúng không
            customerPage = customerRepository.searchCustomerKeyword(keyword, pageable);
        } catch (Exception e) {
            // Log lỗi nếu có và trả về một Page rỗng hoặc xử lý theo cách khác

            customerPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // Chuyển đổi và trả về Page<CustomerDto>
        return customerPage.map(this::convertToDto);
    }

    private CustomerDto convertToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setCode(customer.getCode());
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        return customerDto;
    }

    private Customer convertToEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setCode(customerDto.getCode());
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setAccount(null);
        customer.setAddressShippings(null);
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        return customer;
    }
}
