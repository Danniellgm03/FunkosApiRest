package com.funkoSpring.funkoSpring.funkos.repositories;

import com.funkoSpring.funkoSpring.funkos.models.Funko;

import java.util.List;
import java.util.Optional;

public interface FunkoRepository {

    List<Funko> getAll();

    Optional<Funko> getById(Long id);

    Funko create(Funko funko);

    Funko update(Long id, Funko funko);

    boolean deleteById(Long id);
}
