package com.example.demo.services.serviceImpl;

import com.example.demo.entities.ProductDetail;
import com.example.demo.entities.TempProductQuantity;
import com.example.demo.repositories.ProductDetailRepository;
import com.example.demo.repositories.TempProductQuantityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TempProductQuantityService {
    private final TempProductQuantityRepository tempProductQuantityRepository;
    private final ProductDetailRepository productDetailRepository;

    public TempProductQuantityService(TempProductQuantityRepository tempProductQuantityRepository, ProductDetailRepository productDetailRepository) {
        this.tempProductQuantityRepository = tempProductQuantityRepository;
        this.productDetailRepository = productDetailRepository;
    }

    @Transactional
    public boolean delete(Long create_id) {
        try {
            List<TempProductQuantity> tempProductQuantities = tempProductQuantityRepository.findAllByIdCreateId(create_id);
            tempProductQuantities.forEach(tempProductQuantity -> {
                delete(create_id, tempProductQuantity.getId().getProductDetailId());
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional
    public void delete(Long create_id, Long product_detail_id) {
        try {
            Optional<TempProductQuantity> tempProductQuantity = tempProductQuantityRepository.findById(new TempProductQuantity.TempProductQuantityId(create_id, product_detail_id));
            Integer quantity = 0;
            if (tempProductQuantity.isPresent()) {
                quantity = tempProductQuantity.get().getQuantity();
            }else{
                return;
            }
            tempProductQuantityRepository.deleteById(new TempProductQuantity.TempProductQuantityId(create_id, product_detail_id));
            Optional<ProductDetail> productDetail = productDetailRepository.findById(product_detail_id);
            if (productDetail.isPresent()) {
                var pd = productDetail.get();
                pd.setQuantity(pd.getQuantity() + quantity);
                productDetailRepository.save(pd);
            }
            ;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Transactional
    public void saveOrUpdate(TempProductQuantity tempProductQuantityDto) {
        Optional<TempProductQuantity> tempProductQuantity = tempProductQuantityRepository.findById(tempProductQuantityDto.getId());

        if (tempProductQuantity.isPresent()) {
            var quantityChange = tempProductQuantity.get().getQuantity() - tempProductQuantityDto.getQuantity();
            tempProductQuantityRepository.save(tempProductQuantityDto);
            ProductDetail productDetail = productDetailRepository.findById(tempProductQuantityDto.getId().getProductDetailId()).get();
            productDetail.setQuantity(productDetail.getQuantity() + quantityChange);
            productDetailRepository.save(productDetail);
        } else {
            tempProductQuantityRepository.save(tempProductQuantityDto);
            ProductDetail productDetail = productDetailRepository.findById(tempProductQuantityDto.getId().getProductDetailId()).get();
            productDetail.setQuantity(productDetail.getQuantity() - tempProductQuantityDto.getQuantity());
            productDetailRepository.save(productDetail);
        }
    }

    public void deleteExpried() {
        var expiredTime = new Date().getTime() + (10* 60 * 1000);
        var expiredItems = tempProductQuantityRepository.findAllByIdCreateIdGreaterThanEqual(expiredTime);
        expiredItems.forEach(expiredItem -> {
            int quantity = 0;
            quantity = expiredItem.getQuantity();
            tempProductQuantityRepository.deleteById(expiredItem.getId());
            Optional<ProductDetail> productDetail = productDetailRepository.findById(expiredItem.getId().getProductDetailId());
            if (productDetail.isPresent()) {
                var pd = productDetail.get();
                pd.setQuantity(pd.getQuantity() + quantity);
                productDetailRepository.save(pd);
            }

        });
    }

    @Transactional
    public void reset() {
        tempProductQuantityRepository.findAll().forEach(temp->{
            delete(temp.getId().getCreateId());
        });
    }
}
