package com.funkoSpring.funkoSpring.funkos.services;

import com.funkoSpring.funkoSpring.funkos.dto.FunkoCreateDto;
import com.funkoSpring.funkoSpring.funkos.dto.FunkoDto;

import java.util.List;

public interface BaseService<T> {

    List<FunkoDto> getAll(String categoria);

    FunkoDto getById(Long id);

    FunkoDto create(FunkoCreateDto elem);

    FunkoDto update(Long id, FunkoCreateDto funko);

    boolean deleteById(Long id);

}
