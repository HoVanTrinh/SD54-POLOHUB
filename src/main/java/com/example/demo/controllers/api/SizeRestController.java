package com.example.demo.controllers.api;

import com.example.demo.dto.Size.SizeDto;
import com.example.demo.entities.Size;
import com.example.demo.services.SizeService;
import com.google.zxing.NotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SizeRestController {
    private final SizeService sizeService;

    public SizeRestController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping("/sizes/{productId}/product")
    public List<Size> getColorsByProductId(@PathVariable Long productId) throws NotFoundException {
        return sizeService.getSizesByProductId(productId);
    }

    @GetMapping("/sizes/{productId}/product/{colorId}/color")
    public List<Size> getSizesByProductIdAndColorId(@PathVariable Long productId, @PathVariable Long colorId) throws NotFoundException {
        return sizeService.getSizesByProductIdAndColorId(productId, colorId);
    }

    @PostMapping("/api/size")
    public SizeDto createSizeApi(@RequestBody SizeDto sizeDto) {
        return sizeService.createSizeApi(sizeDto);
    }
    @GetMapping("/api/check-size-name")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkProductName(@RequestParam String name) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", sizeService.existsByName(name));
        return ResponseEntity.ok(response);
    }
}
