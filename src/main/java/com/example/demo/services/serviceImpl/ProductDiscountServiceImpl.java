package com.example.demo.services.serviceImpl;

import com.example.demo.dto.ProductDiscount.ProductDiscountCreateDto;
import com.example.demo.dto.ProductDiscount.ProductDiscountDto;
import com.example.demo.entities.Color;
import com.example.demo.entities.ProductDetail;
import com.example.demo.entities.ProductDiscount;
import com.example.demo.entities.Size;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repositories.ProductDetailRepository;
import com.example.demo.repositories.ProductDiscountRepository;
import com.example.demo.services.ProductDiscountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductDiscountServiceImpl implements ProductDiscountService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductDiscountRepository productDiscountRepository;

    public ProductDiscountServiceImpl(ProductDetailRepository productDetailRepository, ProductDiscountRepository productDiscountRepository) {
        this.productDetailRepository = productDetailRepository;
        this.productDiscountRepository = productDiscountRepository;
    }

    @Override
    public List<ProductDiscount> getAllProductDiscount() {
        return productDiscountRepository.findAll();
    }

    @Override
    public List<ProductDiscount> getAllProductDiscount(Color color, Size size) {
        if (color != null && size != null) {
            return productDiscountRepository.findByProductDetail_ColorAndProductDetail_Size(color, size);
        } else if (color != null) {
            return productDiscountRepository.findByProductDetail_Color(color);
        } else if (size != null) {
            return productDiscountRepository.findByProductDetail_Size(size);
        } else {
            return productDiscountRepository.findAll();
        }
    }

    @Override
    public List<ProductDiscount> getDiscountsByColor(Color color) {
        return productDiscountRepository.findByProductDetail_Color(color);
    }

    @Override
    public List<ProductDiscount> getDiscountsBySize(Size size) {
        return productDiscountRepository.findByProductDetail_Size(size);
    }

    @Override
    public ProductDiscountDto updateCloseProductDiscount(Long discountId, boolean closed) {
        ProductDiscount productDiscount = productDiscountRepository.findById(discountId)
                .orElseThrow(() -> new NotFoundException("Product discount not found"));
        productDiscount.setClosed(closed);
        return convertToDto(productDiscountRepository.save(productDiscount));
    }

    @Override
    public List<ProductDiscountDto> createProductDiscountMultiple(ProductDiscountCreateDto productDiscountCreateDto) {
        List<ProductDiscount> productDiscountList = productDiscountCreateDto.getProductDiscounts().stream().map(productDiscountDto -> {
            ProductDiscount productDiscount = productDiscountRepository.findByProductDetail_Id(productDiscountDto.getProductDetailId());
            if (productDiscount != null) {
                productDiscountDto.setId(productDiscount.getId());
            }
            productDiscountDto.setClosed(false);
            return convertToEntity(productDiscountDto);
        }).collect(Collectors.toList());
        productDiscountRepository.saveAll(productDiscountList);
        return productDiscountCreateDto.getProductDiscounts();
    }

    private ProductDiscount convertToEntity(ProductDiscountDto productDiscountDto) {
        ProductDiscount productDiscount = new ProductDiscount();
        productDiscount.setId(productDiscountDto.getId());
        productDiscount.setDiscountedAmount(productDiscountDto.getDiscountedAmount());
        productDiscount.setClosed(productDiscountDto.isClosed());
        productDiscount.setStartDate(productDiscountDto.getStartDate());
        productDiscount.setEndDate(productDiscountDto.getEndDate());
        ProductDetail productDetail = productDetailRepository.findById(productDiscountDto.getProductDetailId())
                .orElseThrow(() -> new NotFoundException("Product detail id not found"));
        productDiscount.setProductDetail(productDetail);
        return productDiscount;
    }

    private ProductDiscountDto convertToDto(ProductDiscount productDiscount) {
        ProductDiscountDto productDiscountDto = new ProductDiscountDto();
        productDiscountDto.setId(productDiscount.getId());
        productDiscountDto.setDiscountedAmount(productDiscount.getDiscountedAmount());
        productDiscountDto.setClosed(productDiscount.isClosed());
        productDiscountDto.setStartDate(productDiscount.getStartDate());
        productDiscountDto.setEndDate(productDiscount.getEndDate());
        productDiscountDto.setProductDetailId(productDiscount.getProductDetail().getId());
        return productDiscountDto;
    }

}
