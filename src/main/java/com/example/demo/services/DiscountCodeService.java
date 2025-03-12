package com.example.demo.services;

import com.example.demo.dto.DiscountCode.DiscountCodeDto;
import com.example.demo.dto.DiscountCode.SearchDiscountCodeDto;
import com.example.demo.entities.DiscountCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface DiscountCodeService {
    Page<DiscountCodeDto> getAllDiscountCode(SearchDiscountCodeDto searchDiscountCodeDto, Integer page);
    DiscountCodeDto saveDiscountCode(DiscountCodeDto discountCodeDto);
    DiscountCodeDto updateDiscountCode(DiscountCodeDto discountCodeDto);

    DiscountCodeDto getDiscountCodeById(Long id);
    DiscountCodeDto getDiscountCodeByCode(Long code);
    DiscountCodeDto updateStatus(Long discountCodeId, int status);
    Page<DiscountCodeDto> getAllAvailableDiscountCode(Pageable pageable);

    Page<DiscountCode> getDiscountCode(Integer page);
}
