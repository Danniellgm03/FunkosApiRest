package com.funkoSpring.funkoSpring.funkos.services;

import com.funkoSpring.funkoSpring.funkos.dto.FunkoCreateDto;
import com.funkoSpring.funkoSpring.funkos.dto.FunkoDto;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoNotFoundException;
import com.funkoSpring.funkoSpring.funkos.mappers.FunkoCreateMapper;
import com.funkoSpring.funkoSpring.funkos.mappers.FunkoMapper;
import com.funkoSpring.funkoSpring.funkos.models.Funko;
import com.funkoSpring.funkoSpring.funkos.repositories.FunkoRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;


import java.util.List;

@Service
@CacheConfig(cacheNames = {"funkos"})
public class FunkoServiceImpl implements BaseService<Funko>{

    private final FunkoRepositoryImpl funkoRepository;



    @Autowired
    public FunkoServiceImpl(FunkoRepositoryImpl funkoRepository) {
        this.funkoRepository = funkoRepository;
    }

    @Override
    public List<FunkoDto> getAll(String categoria) {
        if(categoria != null){
            return funkoRepository.getAll().stream().filter(funko -> funko.getCategoria().equalsIgnoreCase(categoria))
                    .map(funko -> FunkoMapper.toFunkoDto(funko)).toList();
        }
        return funkoRepository.getAll().stream().map(funko -> FunkoMapper.toFunkoDto(funko)).toList();
    }

    @Override
    @Cacheable
    public FunkoDto getById(Long id) {
        return funkoRepository.getById(id).map(funko -> FunkoMapper.toFunkoDto(funko)).orElseThrow(() -> new FunkoNotFoundException(id));
    }

    @Override
    public FunkoDto create(FunkoCreateDto funko) {
        if(funko == null){
            throw new IllegalArgumentException("El funko no puede ser null");
        }
        return FunkoMapper.toFunkoDto(funkoRepository.create(FunkoCreateMapper.toFunko(funko)));
    }

    @Override
    public FunkoDto update(Long id, FunkoCreateDto funkoCreateDto) throws FunkoNotFoundException{
        return FunkoMapper.toFunkoDto(funkoRepository.update(id, FunkoCreateMapper.toFunko(funkoCreateDto)));
    }

    @Override
    public boolean deleteById(Long id) throws FunkoNotFoundException {
        getById(id);
        return funkoRepository.deleteById(id);
    }
}
