package com.example.demo.controllers.api;

import com.example.demo.dto.Material.MaterialDto;
import com.example.demo.services.MaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MaterialRestController {
    private final MaterialService materialService;

    public MaterialRestController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping("/api/material")
    public MaterialDto createMaterialApi(@RequestBody MaterialDto materialDto) {
        return materialService.createMaterialApi(materialDto);
    }
    @GetMapping("/api/check-material-name")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkProductName(@RequestParam String name) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", materialService.existsByName(name));
        return ResponseEntity.ok(response);
    }
}
