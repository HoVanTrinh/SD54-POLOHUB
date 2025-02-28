package com.example.demo.dto.Color;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColorDto {
    private Long id;
    private String code;
    private String name;
}
