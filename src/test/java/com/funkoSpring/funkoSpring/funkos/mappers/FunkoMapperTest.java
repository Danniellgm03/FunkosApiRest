package com.funkoSpring.funkoSpring.funkos.mappers;

import com.funkoSpring.funkoSpring.funkos.dto.FunkoDto;
import com.funkoSpring.funkoSpring.funkos.models.Funko;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class FunkoMapperTest {

    Funko funko = Funko.builder()
            .id(1L)
            .nombre("Funko 1")
            .precio(10.0)
            .imagen("imagen1")
            .categoria("categoria1")
            .cantidad(1)
            .build();
    @Test
    void toFunkoDto() {
        FunkoDto funkoDto = FunkoMapper.toFunkoDto(funko);

        assertAll(
                () -> assertEquals(funko.getId(), funkoDto.getId()),
                () -> assertEquals(funko.getNombre(), funkoDto.getNombre()),
                () -> assertEquals(funko.getCategoria(), funkoDto.getCategoria()),
                () -> assertEquals(funko.getCantidad(), funkoDto.getCantidad())
        );
    }

    @Test
    void toFunko(){
        Funko funko = FunkoMapper.toFunko(FunkoMapper.toFunkoDto(this.funko));

        assertAll(
                () -> assertEquals(this.funko.getId(), funko.getId()),
                () -> assertEquals(this.funko.getNombre(), funko.getNombre()),
                () -> assertEquals(this.funko.getCategoria(), funko.getCategoria()),
                () -> assertEquals(this.funko.getCantidad(), funko.getCantidad())
        );
    }

}
