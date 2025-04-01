package com.example.demo.repositories;

import com.example.demo.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByCode(String code);
}
