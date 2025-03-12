package com.example.demo.repositories.Specification;

import com.example.demo.dto.DiscountCode.SearchDiscountCodeDto;
import com.example.demo.entities.DiscountCode;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiscountCodeSpec implements Specification<DiscountCode> {
    private SearchDiscountCodeDto searchDiscountCodeDto;

    public DiscountCodeSpec(SearchDiscountCodeDto searchRequest) {
        this.searchDiscountCodeDto = searchRequest;
    }

    @Override
    public Predicate toPredicate(Root<DiscountCode> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (searchDiscountCodeDto.getKeyword() != null) {
            String keyword = searchDiscountCodeDto.getKeyword();

            Predicate productNamePredicate = criteriaBuilder.like(root.get("code"), "%" + keyword + "%");
            Predicate productCodePredicate = criteriaBuilder.like(root.get("detail"), "%" + keyword + "%");
            Predicate combinedPredicate = criteriaBuilder.or(productNamePredicate, productCodePredicate);

            predicates.add(combinedPredicate);
        }
        if (searchDiscountCodeDto.getCode() != null) {
            Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + searchDiscountCodeDto.getCode() + "%");
            predicates.add(predicate);
        }

        if (searchDiscountCodeDto.getDetail() != null) {
            Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("detail")), "%" + searchDiscountCodeDto.getDetail() + "%");
            predicates.add(predicate);
        }

        if (searchDiscountCodeDto.getStartDate() != null) {
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), searchDiscountCodeDto.getStartDate());
            predicates.add(predicate);
        }

        if (searchDiscountCodeDto.getEndDate() != null) {
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), searchDiscountCodeDto.getEndDate());
            predicates.add(predicate);
        }

        if (searchDiscountCodeDto.getDiscountCodeType() != null) {
            Predicate predicate = criteriaBuilder.equal(root.get("type"), searchDiscountCodeDto.getDiscountCodeType());
            predicates.add(predicate);
        }

        if (searchDiscountCodeDto.getMaximumUsage() != null) {
            Predicate predicate = criteriaBuilder.equal(root.get("maximumUsage"), searchDiscountCodeDto.getMaximumUsage());
            predicates.add(predicate);
        }

        if (searchDiscountCodeDto.getStatus() != null) {
            if (searchDiscountCodeDto.getStatus() == 1) {
                Predicate notExpiration = criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), new Date());
                Predicate hasBegin = criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), new Date());
                predicates.add(notExpiration);
                predicates.add(hasBegin);
            }

            if (searchDiscountCodeDto.getStatus() == 2) {
                Predicate predicate = criteriaBuilder.lessThan(root.get("endDate"), new Date());
                predicates.add(predicate);
            } else {
                Predicate predicate = criteriaBuilder.equal(root.get("status"), searchDiscountCodeDto.getStatus());
                predicates.add(predicate);
            }
        }


//        predicates.add(criteriaBuilder.equal(root.get("status"), 1));
        predicates.add(criteriaBuilder.equal(root.get("deleteFlag"), false));
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
