package com.funkoSpring.funkoSpring.funkos.repositories;

import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoAlreadyExistException;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoNotFoundException;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoNullException;
import com.funkoSpring.funkoSpring.funkos.models.Funko;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDateTime;
import java.util.List;

public class FunkoRepositoryTest {

    private FunkoRepositoryImpl repository;

    @BeforeEach
    void setUp(){
        repository = new FunkoRepositoryImpl();
    }

    @Test
    void getAll(){
        repository.deleteById(1L);
        repository.deleteById(2L);
        repository.deleteById(3L);
        repository.create(Funko.builder().nombre("Funko 0").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build());
        List<Funko> funkos = repository.getAll();
        assertAll(
                () -> assertFalse(funkos.isEmpty()),
                () -> assertEquals(1, funkos.size())
        );
    }

    @Test
    void getById(){
        Funko funko = repository.getById(1L).get();

        assertAll(
                () -> assertEquals("Funko 0", funko.getNombre()),
                () -> assertNotNull(funko),
                () -> assertEquals("Anime", funko.getCategoria())
        );
    }

    @Test
    void getByIdNotFound(){
        assertAll(
                () -> assertFalse(repository.getById(666L).isPresent())
        );
    }

    @Test
    void create(){
        Funko funko_created = repository.create(Funko.builder().nombre("Funko 0").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build());
        Funko funko_created2 = repository.create(Funko.builder().nombre("Funko 0").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build());
        assertAll(
                () -> assertNotNull(funko_created),
                () -> assertEquals("Funko 0", funko_created.getNombre()),
                () -> assertTrue(funko_created.getId() < funko_created2.getId()),
                () -> assertThrowsExactly(FunkoNullException.class, () -> repository.create(null))
        );
    }

    @Test
    void creatFunkoAlreadyExist(){
        assertAll(
                () -> assertThrowsExactly(FunkoAlreadyExistException.class, () -> repository.create(Funko.builder().id(1L).nombre("Funko 0").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build()))
        );
    }

    @Test
    void update() throws InterruptedException {
        Funko funko_created = repository.create(Funko.builder().nombre("Funko 0").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build());

        Thread.sleep(1000);
        Funko funko = Funko.builder().nombre("pepe").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build();
        Funko funko_update = repository.update(funko_created.getId(), funko);

        assertAll(
                () -> assertEquals("pepe", funko_update.getNombre()),
                () -> assertTrue(funko_created.getFecha_actualizacion().isBefore(funko_update.getFecha_actualizacion()))
        );
    }

    @Test
    void updateNotFound(){
        Funko funko = Funko.builder().nombre("pepe").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build();
        assertAll(
                () -> assertThrowsExactly(FunkoNotFoundException.class, () -> repository.update(666L, funko))
        );
    }

    @Test
    void deleteById(){
        repository.deleteById(1L);
        repository.deleteById(2L);
        repository.deleteById(3L);
        List<Funko> funkos = repository.getAll();
        assertAll(
                () -> assertTrue(funkos.isEmpty())
        );

        repository.create(Funko.builder().nombre("Funko 0").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build());
        repository.create(Funko.builder().nombre("Funko 0").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build());
        repository.deleteById(5L);
        List<Funko> funkos2 = repository.getAll();
        assertAll(
                () -> assertFalse(funkos2.isEmpty()),
                () -> assertEquals(1, funkos2.size())
        );
    }

    @Test
    void deleteByIdNotFound(){
        assertAll(
                () -> assertThrowsExactly(FunkoNotFoundException.class, () -> repository.deleteById(99L))
        );
    }

}
