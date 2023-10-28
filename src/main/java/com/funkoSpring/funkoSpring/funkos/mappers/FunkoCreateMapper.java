package com.funkoSpring.funkoSpring.funkos.mappers;

import com.funkoSpring.funkoSpring.funkos.dto.FunkoCreateDto;
import com.funkoSpring.funkoSpring.funkos.dto.FunkoDto;
import com.funkoSpring.funkoSpring.funkos.models.Funko;

public class FunkoCreateMapper {

    public static  FunkoCreateDto toFunkoCreateDto(Funko funko){
        return new FunkoCreateDto(funko.getNombre(), funko.getPrecio(), funko.getImagen(), funko.getCategoria(), funko.getCantidad());
    }

    public static Funko toFunko(FunkoCreateDto funkoDto){
        return Funko.builder()
                .nombre(funkoDto.getNombre())
                .precio(funkoDto.getPrecio())
                .imagen(funkoDto.getImagen())
                .categoria(funkoDto.getCategoria())
                .cantidad(funkoDto.getCantidad())
                .build();
    }

}
