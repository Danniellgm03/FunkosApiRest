package com.funkoSpring.funkoSpring.funkos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunkoDto {
    private Long id;
    private String nombre;
    private double precio;
    private String imagen;
    private String categoria;
    private int cantidad;
}
