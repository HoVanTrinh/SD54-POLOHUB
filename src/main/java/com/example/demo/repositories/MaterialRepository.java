package com.example.demo.repositories;

import com.example.demo.entities.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    boolean existsByCode(String code);
    List<Material> findAllByDeleteFlagFalse();
    boolean existsByName(String name);
    Page<Material> findAllByDeleteFlagFalse(Pageable pageable);
}

