package com.funkoSpring.funkoSpring.funkos.mappers;

import com.funkoSpring.funkoSpring.funkos.dto.FunkoDto;
import com.funkoSpring.funkoSpring.funkos.models.Funko;
import org.springframework.stereotype.Component;

@Component
public class FunkoMapper {

    public static FunkoDto toFunkoDto(Funko funko){
        return new FunkoDto(funko.getId(), funko.getNombre(), funko.getPrecio(), funko.getImagen(), funko.getCategoria(), funko.getCantidad());
    }

    public static Funko toFunko(FunkoDto funkoDto){
        return Funko.builder()
                .id(funkoDto.getId())
                .nombre(funkoDto.getNombre())
                .precio(funkoDto.getPrecio())
                .imagen(funkoDto.getImagen())
                .categoria(funkoDto.getCategoria())
                .cantidad(funkoDto.getCantidad())
                .build();
    }

}
