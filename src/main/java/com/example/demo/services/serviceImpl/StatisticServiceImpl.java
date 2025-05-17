package com.example.demo.services.serviceImpl;

import com.example.demo.dto.Statistic.BestSellerProduct;
import com.example.demo.dto.Statistic.OrderStatistic;
import com.example.demo.dto.Statistic.ProductStatistic;
import com.example.demo.repositories.BillRepository;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.StatisticService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final BillRepository billRepository;
    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;

    public StatisticServiceImpl(BillRepository billRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.billRepository = billRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }





    @Override
    public List<ProductStatistic> getStatisticProductInTime(String fromDate, String toDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Kiểm tra giá trị null hoặc undefined và gán giá trị mặc định nếu cần
        if (fromDate == null || fromDate.isEmpty() || "undefined".equals(fromDate)) {
            System.out.println("Giá trị fromDate không hợp lệ: " + fromDate);
            throw new IllegalArgumentException("Ngày bắt đầu không hợp lệ");
        }
        if (toDate == null || toDate.isEmpty() || "undefined".equals(toDate)) {
            System.out.println("Giá trị toDate không hợp lệ: " + toDate);
            throw new IllegalArgumentException("Ngày kết thúc không hợp lệ");
        }

// Định dạng lại từ chuỗi đầu vào
        String formattedFromDate = LocalDateTime.parse(fromDate + "T00:00:00").format(formatter);
        String formattedToDate = LocalDateTime.parse(toDate + "T23:59:59").format(formatter);
        return productRepository.getStatisticProduct(formattedFromDate, formattedToDate);
    }

    @Override
    public List<OrderStatistic> getStatisticOrder() {
        return billRepository.statisticOrder();
    }







    @Override
    public List<BestSellerProduct> getBestSellerProduct(String fromDate, String toDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedFromDate = LocalDateTime.parse(fromDate + "T00:00:00").format(formatter);
        String formattedToDate = LocalDateTime.parse(toDate + "T23:59:59").format(formatter);
        return productRepository.getBestSellerProduct(formattedFromDate, formattedToDate);
    }



    @Override
    public List<BestSellerProduct> getBestSellerProductAll() {
        return productRepository.getBestSellerProduct();
    }
}
