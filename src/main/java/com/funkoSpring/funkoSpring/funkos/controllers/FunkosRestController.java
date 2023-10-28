package com.funkoSpring.funkoSpring.funkos.controllers;
import com.funkoSpring.funkoSpring.funkos.dto.FunkoCreateDto;
import com.funkoSpring.funkoSpring.funkos.dto.FunkoDto;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoAlreadyExistException;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoNotFoundException;
import com.funkoSpring.funkoSpring.funkos.models.ErrorResponse;
import com.funkoSpring.funkoSpring.funkos.services.FunkoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/funkos")
public class FunkosRestController {

    @Autowired
    private FunkoServiceImpl funkoService;


    @GetMapping
    public ResponseEntity<List<FunkoDto>> getFunkos(@RequestParam(required = false) String categoria) {
        return ResponseEntity.ok(funkoService.getAll(categoria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FunkoDto> getFunkoByID(@PathVariable Long id){
        return ResponseEntity.ok(funkoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<FunkoDto> createFunko(@Valid @RequestBody FunkoCreateDto funkoCreateDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(funkoService.create(funkoCreateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FunkoDto> updateFunko(@PathVariable Long id, @Valid @RequestBody FunkoCreateDto funkoCreateDto) {
        return ResponseEntity.ok(funkoService.update(id, funkoCreateDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FunkoDto> updatedFunkoPatch(@PathVariable Long id,@Valid @RequestBody FunkoCreateDto funkoCreateDto) {
        return ResponseEntity.ok(funkoService.update(id, funkoCreateDto));
    }

    @DeleteMapping("/{id}")
    public String deleteFunko(@PathVariable Long id){
        return funkoService.deleteById(id) ? "El funko con id " + id + " fue borrado" : null;
    }

    @ExceptionHandler(FunkoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFunkoNotFound(FunkoNotFoundException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(FunkoAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFunkoNotFound(FunkoAlreadyExistException exception){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
