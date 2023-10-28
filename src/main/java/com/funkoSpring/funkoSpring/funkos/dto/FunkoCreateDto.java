package com.funkoSpring.funkoSpring.funkos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FunkoCreateDto {
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @Min(value = 0, message = "El precio no puede ser negativo")
    @NotNull(message = "El precio es obligatorio")
    private Double precio;

    private String imagen;
    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

}
