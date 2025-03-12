package com.example.demo.repositories;

import com.example.demo.entities.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
}
