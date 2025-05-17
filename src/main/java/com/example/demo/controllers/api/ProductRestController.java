package com.example.demo.controllers.api;


import com.example.demo.dto.Statistic.BestSellerProduct;
import com.example.demo.dto.product.ProductDto;
import com.example.demo.dto.product.SearchProductDto;
import com.example.demo.services.ProductService;
import com.example.demo.services.StatisticService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductRestController{
    private final ProductService productService;
    private  final StatisticService statisticService;

    public ProductRestController(ProductService productService, StatisticService statisticService) {
        this.productService = productService;
        this.statisticService = statisticService;

    }

    @GetMapping("/api/products")
    public Page<ProductDto> getAllProductsApi(@PageableDefault(page = 0, size = 12) Pageable pageable) {
        return productService.getAllProductApi(pageable);
    }

    //    @CrossOrigin(origins = "http://localhost:3000") // Thay thế bằng domain của bạn
    @GetMapping("/api/products-no-pagination")
    public List<ProductDto> getAllProductsApi(SearchProductDto searchRequest) {
        if (searchRequest == null) {
            searchRequest = new SearchProductDto();
        }
//        System.out.println("Search request received: " + searchRequest); // Log để kiểm tra
        return productService.getAllProductNoPaginationApi(searchRequest);
    }

    @GetMapping("/api/products/filter")
    public Page<ProductDto> filterProductApi(SearchProductDto searchRequest, @PageableDefault(page = 0, size = 10) Pageable page){
        return productService.searchProduct(searchRequest, page);
    }

    @GetMapping("/api/products/getByBarcode")
    public ProductDto filterProductApi(@RequestParam String barcode){
        return productService.getProductByBarcode(barcode);
    }

    @GetMapping("/api/products/{detailId}/productDetail")
    public ProductDto getByProductDetailId(@PathVariable Long detailId) {
        return productService.getByProductDetailId(detailId);
    }
    @GetMapping("/api/check-product-name")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkProductName(@RequestParam String name) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", productService.existsByName(name));
        return ResponseEntity.ok(response);
    }
    @GetMapping("/api/get-bestseller-product")
    private List<BestSellerProduct> getBestSellerProductInTime(@RequestParam String fromDate, @RequestParam String toDate) {
        return statisticService.getBestSellerProduct(fromDate, toDate);
    }

    @GetMapping("/api/get-bestseller-product-all")
    private List<BestSellerProduct> getBestSellerProductAll() {
        return statisticService.getBestSellerProductAll();
    }
}
