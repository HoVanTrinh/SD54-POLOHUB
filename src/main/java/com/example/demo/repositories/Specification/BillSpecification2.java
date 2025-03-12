package com.example.demo.repositories.Specification;

import com.example.demo.dto.Bill.SearchBillDto;
import com.example.demo.entities.Bill;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BillSpecification2 implements Specification<Bill> {
    private SearchBillDto searchBillDto;

    public BillSpecification2(SearchBillDto searchBillDto) {
        this.searchBillDto = searchBillDto;
    }

    @Override
    public Predicate toPredicate(Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(searchBillDto.getKeyword() != null) {
            String keyword = searchBillDto.getKeyword();

            Predicate billCodePredicate = criteriaBuilder.like(root.get("code"), "%" + keyword + "%");
            Predicate customerNamePredicate = criteriaBuilder.like(root.get("customer").get("name"), "%" + keyword + "%");
            Predicate phoneNumberPredicate = criteriaBuilder.like(root.get("customer").get("phoneNumber"), "%" + keyword + "%");

            Predicate combinedPredicate = criteriaBuilder.or(billCodePredicate, customerNamePredicate, phoneNumberPredicate);

            predicates.add(combinedPredicate);
        }

        if(searchBillDto.getFromDate() != null) {
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"),  searchBillDto.getFromDate());
            predicates.add(predicate);
        }

        if(searchBillDto.getToDate() != null) {
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), searchBillDto.getToDate() );
            predicates.add(predicate);
        }

        if(searchBillDto.getBillStatus() != null) {
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("status"), searchBillDto.getBillStatus() );
            predicates.add(predicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
