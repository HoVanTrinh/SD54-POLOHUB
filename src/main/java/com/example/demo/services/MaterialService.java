package com.example.demo.services;

import com.example.demo.dto.Material.MaterialDto;
import com.example.demo.entities.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MaterialService {
    Page<Material> getAllMaterial(Pageable pageable);

    Material save(Material material);

    Material createMaterial(Material material);
    Material updateMaterial(Material material);

    void delete(Long id);

    Optional<Material> findById(Long id);
    List<Material> getAll();
    boolean existsByName(String name);
    MaterialDto createMaterialApi(MaterialDto materialDto);
}
