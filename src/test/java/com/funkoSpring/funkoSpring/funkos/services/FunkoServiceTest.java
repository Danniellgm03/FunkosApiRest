package com.funkoSpring.funkoSpring.funkos.services;

import com.funkoSpring.funkoSpring.funkos.dto.FunkoDto;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoAlreadyExistException;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoNotFoundException;
import com.funkoSpring.funkoSpring.funkos.mappers.FunkoCreateMapper;
import com.funkoSpring.funkoSpring.funkos.models.Funko;
import com.funkoSpring.funkoSpring.funkos.repositories.FunkoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FunkoServiceTest {

    List<Funko> funkos;
    @Mock
    private FunkoRepositoryImpl funkoRepository;

    @InjectMocks
    private FunkoServiceImpl funkoService;

    @BeforeEach
    void setUp(){
        funkos = List.of(
                Funko.builder().id(1L).nombre("Funko 0").precio(20).cantidad(30).imagen("https://prueba1.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build(),
                Funko.builder().id(2L).nombre("Funko 1").precio(40).cantidad(20).imagen("https://prueba2.jpg").categoria("Marvel").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build(),
                Funko.builder().id(3L).nombre("Funko 2").precio(60).cantidad(10).imagen("https://prueba3.jpg").categoria("Comic").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build(),
                Funko.builder().id(4L).nombre("Funko 3").precio(80).cantidad(1).imagen("https://prueba4.jpg").categoria("TWD").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build()
        );
    }

    @Test
    void getAll(){
        when(funkoRepository.getAll()).thenReturn(funkos);

        List<FunkoDto> funkosServ = funkoService.getAll(null);
        assertAll(
                () -> assertFalse(funkosServ.isEmpty()),
                () -> assertEquals(4, funkosServ.size())
        );

        verify(funkoRepository, times(1)).getAll();
    }

    @Test
    void getAllWithCategories(){
        when(funkoRepository.getAll()).thenReturn(funkos);

        List<FunkoDto> funkosServ = funkoService.getAll("Anime");
        List<FunkoDto> funkosServCaseNoSensitive = funkoService.getAll("AnImE");
        assertAll(
                () -> assertFalse(funkosServ.isEmpty()),
                () -> assertEquals(1, funkosServ.size()),
                () -> assertEquals(1, funkosServCaseNoSensitive.size())
        );



        verify(funkoRepository, times(2)).getAll();
    }

    @Test
    void getById(){
        when(funkoRepository.getById(1L)).thenReturn(Optional.of(funkos.get(0)));
        FunkoDto funko = funkoService.getById(1L);
        assertAll(
                () -> assertEquals(funko.getNombre(), funkos.get(0).getNombre()),
                () -> assertEquals(funko.getId(), funkos.get(0).getId())
        );

        verify(funkoRepository, times(1)).getById(1L);
    }

    @Test
    void getByIdNotFound(){
        when(funkoRepository.getById(99L)).thenReturn(Optional.empty());

        var res = assertThrows(FunkoNotFoundException.class, () -> funkoService.getById(99L));

        assertEquals(res.getMessage(), "El funko con id:99 no existe");

        verify(funkoRepository, times(1)).getById(99L);
    }

    @Test
    void create(){
        when(funkoRepository.create(any(Funko.class))).thenReturn(funkos.get(0));

        FunkoDto funkoDto = funkoService.create(FunkoCreateMapper.toFunkoCreateDto(funkos.get(0)));

        assertAll(
                () -> assertEquals(funkoDto.getNombre(), funkos.get(0).getNombre()),
                () -> assertEquals(funkoDto.getId(), funkos.get(0).getId())
        );

        verify(funkoRepository, times(1)).create(any(Funko.class));
    }

    @Test
    void createAlreadyExist(){
        when(funkoRepository.create(any(Funko.class))).thenReturn(funkos.get(0));
        funkoService.create(FunkoCreateMapper.toFunkoCreateDto(funkos.get(0)));

        when(funkoRepository.create(any(Funko.class))).thenThrow(new FunkoAlreadyExistException("El funko con el id " + funkos.get(0).getId() + " ya existe"));
        var res = assertThrows(FunkoAlreadyExistException.class, () -> funkoService.create(FunkoCreateMapper.toFunkoCreateDto(funkos.get(0))));

        assertEquals(res.getMessage(), "El funko con el id " + funkos.get(0).getId() + " ya existe");

        verify(funkoRepository, times(2)).create(any(Funko.class));
    }

    @Test
    void createNullException(){
        var res = assertThrows(IllegalArgumentException.class, () -> funkoService.create(null));

        assertEquals(res.getMessage(), "El funko no puede ser null");

        verify(funkoRepository, times(0)).create(null);
    }

    @Test
    void update(){
        when(funkoRepository.update(any(Long.class), any(Funko.class))).thenReturn(funkos.get(0));

        funkos.get(0).setNombre("Funko 0 updated");
        FunkoDto funkoDto = funkoService.update(1L, FunkoCreateMapper.toFunkoCreateDto(funkos.get(0)));

        assertAll(
                () -> assertEquals(funkoDto.getNombre(), funkos.get(0).getNombre()),
                () -> assertEquals(funkoDto.getId(), funkos.get(0).getId()),
                () -> assertEquals(funkoDto.getNombre(), "Funko 0 updated")
        );

        verify(funkoRepository, times(1)).update(any(Long.class), any(Funko.class));
    }

    @Test
    void updateNotFound(){
        when(funkoRepository.update(any(Long.class), any(Funko.class))).thenThrow(new FunkoNotFoundException(99L));

        var res = assertThrows(FunkoNotFoundException.class, () -> funkoService.update(99L, FunkoCreateMapper.toFunkoCreateDto(funkos.get(0))));

        assertEquals(res.getMessage(), "El funko con id:99 no existe");

        verify(funkoRepository, times(1)).update(any(Long.class), any(Funko.class));
    }

    @Test
    void deleteById(){
        when(funkoRepository.deleteById(any(Long.class))).thenReturn(true);
        when(funkoRepository.getById(any(Long.class))).thenReturn(Optional.of(funkos.get(0)));
        boolean res = funkoService.deleteById(1L);

        assertTrue(res);

        verify(funkoRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    void deleteByIdNotFound(){
        when(funkoRepository.getById(any(Long.class))).thenReturn(Optional.empty());
        var res = assertThrows(FunkoNotFoundException.class, () -> funkoService.deleteById(99L));

        assertEquals(res.getMessage(), "El funko con id:99 no existe");

        verify(funkoRepository, times(0)).deleteById(any(Long.class));
    }



}
