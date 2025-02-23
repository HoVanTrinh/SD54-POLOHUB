package com.example.demo.repositories;

import com.example.demo.entities.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {
    // Thêm phương thức tùy chỉnh để tìm `Color` theo tên
    Optional<Color> findByName(String name);
}
