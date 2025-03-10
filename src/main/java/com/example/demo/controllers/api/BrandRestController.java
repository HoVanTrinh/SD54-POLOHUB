package com.example.demo.controllers.api;

import com.example.demo.dto.Brand.BrandDto;
import com.example.demo.services.BrandService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandRestController {
    private final BrandService brandService;

    public BrandRestController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/api/brand")
    public BrandDto createBrandApi(@RequestBody @Valid BrandDto brandDto) {
        return brandService.createBrandApi(brandDto);
    }
}
