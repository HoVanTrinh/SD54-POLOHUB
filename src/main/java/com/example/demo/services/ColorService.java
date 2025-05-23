package com.example.demo.services;

import com.example.demo.dto.Color.ColorDto;
import com.example.demo.entities.Color;
import com.example.demo.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ColorService {
    Color updateColor(Color color);
    Color createColor(Color color);
    void delete(Long id);
    List<Color> findAll();
    Optional<Color> findById(Long id);
    boolean existsById(Long id);
    Page<Color> findAll(Integer page, Integer limit);
    Page<Color> findAll(Pageable pageable);
    List<Color> getColorByProductId(Long productId) throws NotFoundException;
    List<Color> getColorsByProductIdAndSizeId(Long productId, Long sizeId) throws NotFoundException;
    ColorDto createColorApi(ColorDto colorDto);
    boolean existsByName(String name);

    void restore(Long id);
}
