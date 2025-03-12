package com.example.demo.repositories;

import com.example.demo.entities.TempProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempProductQuantityRepository extends JpaRepository<TempProductQuantity, TempProductQuantity.TempProductQuantityId> {
    List<TempProductQuantity> findAllByIdCreateId(Long createId);

    List<TempProductQuantity> findAllByIdCreateIdGreaterThanEqual(Long expiredTime);
}
