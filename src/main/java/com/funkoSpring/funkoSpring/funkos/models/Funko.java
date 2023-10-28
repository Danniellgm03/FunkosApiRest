package com.funkoSpring.funkoSpring.funkos.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Funko {
    private Long id;
    private String nombre;
    private double precio;
    private int cantidad;
    private String imagen;
    private String categoria;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

}
