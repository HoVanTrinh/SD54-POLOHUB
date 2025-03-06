package com.example.demo.services;

import com.example.demo.dto.Brand.BrandDto;
import com.example.demo.entities.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    Page<Brand> getAllBrand(Pageable pageable);

    Brand createBrand(Brand brand);
    Brand updateBrand(Long id, Brand brand);
    Brand save(Brand brand);

    void delete(Long id);

    Optional<Brand> findById(Long id);

    List<Brand> getAll();

    BrandDto createBrandApi(BrandDto brandDto);

    boolean existsById(Long id);
}
