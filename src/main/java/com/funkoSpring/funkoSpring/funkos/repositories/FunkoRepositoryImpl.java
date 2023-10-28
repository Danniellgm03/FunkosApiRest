package com.funkoSpring.funkoSpring.funkos.repositories;

import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoAlreadyExistException;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoException;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoNotFoundException;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoNullException;
import com.funkoSpring.funkoSpring.funkos.models.Funko;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class FunkoRepositoryImpl implements FunkoRepository{

    private final Map<Long, Funko> funkos;
    private long id = 1L;
    public FunkoRepositoryImpl() {
        this.funkos = new LinkedHashMap<>();
        funkos.put(id, Funko.builder().id(id).nombre("Funko 0").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build());
        id++;
        funkos.put(id, Funko.builder().id(id).nombre("Funko 1").precio(40).cantidad(20).imagen("https://prueba1.jpg").categoria("Marvel").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build());
        id++;
        funkos.put(id, Funko.builder().id(id).nombre("Funko 2").precio(60).cantidad(10).imagen("https://prueba2.jpg").categoria("Comico").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build());
    }

    @Override
    public List<Funko> getAll() {
        return List.copyOf(funkos.values()).stream().sorted(Comparator.comparingLong(Funko::getId)).toList();
    }

    @Override
    public Optional<Funko> getById(Long id) {
        return Optional.ofNullable(funkos.get(id));
    }

    @Override
    public Funko create(Funko funko) throws FunkoNullException{
        if(funko == null){
            throw new FunkoNullException("El funko no puede ser null");
        }

        if(funko.getId() == null){
            id++;
            funko.setId(id);
        }else if(getById(funko.getId()).get() != null){
            throw new FunkoAlreadyExistException("El funko con el id " + funko.getId() + " ya existe");
        }
        funko.setFecha_creacion(LocalDateTime.now());
        funko.setFecha_actualizacion(LocalDateTime.now());
        funkos.put(funko.getId(), funko);
        return funkos.get(funko.getId());
    }

    @Override
    public Funko update(Long id,Funko funko) throws FunkoNotFoundException{
        if(funkos.get(id) != null){
            funko.setId(id);
            funko.setFecha_creacion(funkos.get(id).getFecha_creacion());
            funko.setFecha_actualizacion(LocalDateTime.now());
            funkos.put(id, funko);
            return funkos.get(id);
        }else{
            throw new FunkoNotFoundException(id);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if(funkos.get(id) == null){
            throw new FunkoNotFoundException(id);
        }
        return funkos.remove(id) != null;
    }
}