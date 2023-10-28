package com.funkoSpring.funkoSpring.funkos.mappers;
import static org.junit.jupiter.api.Assertions.*;

import com.funkoSpring.funkoSpring.funkos.dto.FunkoCreateDto;
import com.funkoSpring.funkoSpring.funkos.models.Funko;
import org.junit.jupiter.api.Test;

public class FunkoCreateMapperTest {

    Funko funko = Funko.builder()
            .id(1L)
            .nombre("Funko 1")
            .precio(10.0)
            .imagen("imagen1")
            .categoria("categoria1")
            .cantidad(1)
            .build();

    @Test
    void toFunkoCreateDto() {
        FunkoCreateDto funkoCreateDto = FunkoCreateMapper.toFunkoCreateDto(funko);

        assertAll(
                () -> assertEquals(funko.getNombre(), funkoCreateDto.getNombre()),
                () -> assertEquals(funko.getPrecio(), funkoCreateDto.getPrecio()),
                () -> assertEquals(funko.getImagen(), funkoCreateDto.getImagen()),
                () -> assertEquals(funko.getCategoria(), funkoCreateDto.getCategoria()),
                () -> assertEquals(funko.getCantidad(), funkoCreateDto.getCantidad())
        );
    }

    @Test
    void toFunko(){
        Funko funko = FunkoCreateMapper.toFunko(FunkoCreateMapper.toFunkoCreateDto(this.funko));

        assertAll(
                () -> assertEquals(this.funko.getNombre(), funko.getNombre()),
                () -> assertEquals(this.funko.getPrecio(), funko.getPrecio()),
                () -> assertEquals(this.funko.getImagen(), funko.getImagen()),
                () -> assertEquals(this.funko.getCategoria(), funko.getCategoria()),
                () -> assertEquals(this.funko.getCantidad(), funko.getCantidad())
        );
    }
}
