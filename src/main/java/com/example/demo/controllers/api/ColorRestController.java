package com.example.demo.controllers.api;

import com.example.demo.dto.Color.ColorDto;
import com.example.demo.entities.Color;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.services.ColorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ColorRestController {
    private final ColorService colorService;

    public ColorRestController(ColorService colorService) {
        this.colorService = colorService;
    }

    @PostMapping("/api/color")
    public ColorDto createColorApi(@RequestBody ColorDto colorDto) {
        return colorService.createColorApi(colorDto);
    }

    @GetMapping("/colors/{productId}/product")
    public List<Color> getColorsByProductId(@PathVariable Long productId) throws NotFoundException {
        return colorService.getColorByProductId(productId);
    }

    @GetMapping("/colors/{productId}/product/{sizeId}/size")
    public List<Color> getColorsByProductIdAndSizeId(@PathVariable Long productId, @PathVariable Long sizeId) throws NotFoundException {
        return colorService.getColorsByProductIdAndSizeId(productId, sizeId);
    }
    @GetMapping("/api/check-color-name")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkProductName(@RequestParam String name) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", colorService.existsByName(name));
        return ResponseEntity.ok(response);
    }
}
