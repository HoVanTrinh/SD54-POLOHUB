package com.example.demo.services;

import com.example.demo.dto.Statistic.BestSellerProduct;
import com.example.demo.dto.Statistic.OrderStatistic;
import com.example.demo.dto.Statistic.ProductStatistic;

import java.util.List;

public interface StatisticService {

    List<BestSellerProduct> getBestSellerProduct(String fromDate, String toDate);

    List<BestSellerProduct> getBestSellerProductAll();


    List<ProductStatistic> getStatisticProductInTime(String fromDate, String toDate);

    List<OrderStatistic> getStatisticOrder();
}
