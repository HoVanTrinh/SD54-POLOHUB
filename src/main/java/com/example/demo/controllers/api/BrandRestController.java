package com.example.demo.controllers.api;

import com.example.demo.dto.Brand.BrandDto;
import com.example.demo.services.BrandService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    @GetMapping("/api/check-brand-name")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkProductName(@RequestParam String name) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", brandService.existsByName(name));
        return ResponseEntity.ok(response);
    }
}
