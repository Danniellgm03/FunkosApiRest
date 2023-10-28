package com.funkoSpring.funkoSpring.funkos.exceptions;

public class FunkoNotFoundException extends FunkoException{
    public FunkoNotFoundException(Long id) {
        super("El funko con id:" + id + " no existe");
    }
}
