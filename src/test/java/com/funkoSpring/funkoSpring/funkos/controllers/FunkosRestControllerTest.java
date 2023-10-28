package com.funkoSpring.funkoSpring.funkos.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funkoSpring.funkoSpring.funkos.dto.FunkoCreateDto;
import com.funkoSpring.funkoSpring.funkos.dto.FunkoDto;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoAlreadyExistException;
import com.funkoSpring.funkoSpring.funkos.exceptions.FunkoNotFoundException;
import com.funkoSpring.funkoSpring.funkos.mappers.FunkoCreateMapper;
import com.funkoSpring.funkoSpring.funkos.mappers.FunkoMapper;
import com.funkoSpring.funkoSpring.funkos.models.ErrorResponse;
import com.funkoSpring.funkoSpring.funkos.models.Funko;
import com.funkoSpring.funkoSpring.funkos.services.FunkoServiceImpl;
import com.funkoSpring.funkoSpring.funkos.services.FunkoServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class FunkosRestControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    FunkoServiceImpl funkoService;

    @Autowired
    MockMvc mockMvc;


    Funko funko = Funko.builder().id(1L).nombre("Funko 0").precio(20).cantidad(30).imagen("https://prueba.jpg").categoria("Anime").fecha_creacion(LocalDateTime.now()).fecha_actualizacion(LocalDateTime.now()).build();

    FunkoDto funkoDto = new FunkoDto(funko.getId(), funko.getNombre(), funko.getPrecio(), funko.getImagen(), funko.getImagen(), funko.getCantidad());

    FunkoCreateDto funkoCreateDto = new FunkoCreateDto(funko.getNombre(), funko.getPrecio(), funko.getImagen(), funko.getCategoria(), funko.getCantidad());

    String endPoint = "/funkos";

    @BeforeEach
    void setUp() {
        funkoDto = new FunkoDto(funko.getId(), funko.getNombre(), funko.getPrecio(), funko.getImagen(), funko.getImagen(), funko.getCantidad());
        funkoCreateDto = new FunkoCreateDto(funko.getNombre(), funko.getPrecio(), funko.getImagen(), funko.getCategoria(), funko.getCantidad());

    }


    @Autowired
    public FunkosRestControllerTest(FunkoServiceImpl funkoService) {
        this.funkoService = funkoService;
    }

    @Test
    void getAllFunkos() throws Exception {
        when(funkoService.getAll(null)).thenReturn(List.of(funkoDto));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        List<FunkoDto> res = mapper.readValue(response.getContentAsString(),mapper.getTypeFactory().constructCollectionType(List.class, FunkoDto.class) );

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(funkoDto.getNombre(), res.get(0).getNombre()),
                () -> assertEquals(1, res.size())
        );

        verify(funkoService, times(1)).getAll(null);
    }

    @Test
    void getAllFunkosCategory() throws Exception {
        when(funkoService.getAll("Anime")).thenReturn(List.of(funkoDto));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint + "?categoria=Anime").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        List<FunkoDto> res = mapper.readValue(response.getContentAsString(),mapper.getTypeFactory().constructCollectionType(List.class, FunkoDto.class) );

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(funkoDto.getNombre(), res.get(0).getNombre()),
                () -> assertEquals(1, res.size())
        );

        verify(funkoService, times(1)).getAll("Anime");
    }

    @Test
    void getAllFunkosCategoriasCaseSensitive() throws Exception {
        when(funkoService.getAll("anime")).thenReturn(List.of(funkoDto));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint + "?categoria=anime").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        List<FunkoDto> res = mapper.readValue(response.getContentAsString(),mapper.getTypeFactory().constructCollectionType(List.class, FunkoDto.class) );

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(funkoDto.getNombre(), res.get(0).getNombre()),
                () -> assertEquals(1, res.size())
        );

        verify(funkoService, times(1)).getAll("anime");
    }

    @Test
    void getFunkoById() throws Exception {
        when(funkoService.getById(1L)).thenReturn(funkoDto);

        MockHttpServletResponse response = mockMvc.perform(get(endPoint + "/1").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        FunkoDto res = mapper.readValue(response.getContentAsString(),FunkoDto.class );

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(funkoDto.getNombre(), res.getNombre()),
                () -> assertEquals(funkoDto.getId(), res.getId())
        );

        verify(funkoService, times(1)).getById(1L);
    }

    @Test
    void getFunkoByIdNotExist() throws Exception {
        when(funkoService.getById(99L)).thenThrow(new FunkoNotFoundException(99L));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint + "/99").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class );

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), res.status() ),
                () -> assertEquals("El funko con id:99 no existe", res.msg())
        );

        verify(funkoService, times(1)).getById(99L);
    }

    @Test
    void createFunko() throws Exception {
        when(funkoService.create(funkoCreateDto)).thenReturn(funkoDto);

        MockHttpServletResponse response = mockMvc.perform(
                post(endPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        FunkoDto res = mapper.readValue(response.getContentAsString(),FunkoDto.class );

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), response.getStatus()),
                () -> assertEquals(funkoDto.getNombre(), res.getNombre()),
                () -> assertEquals(funkoDto.getId(), res.getId())
        );

        verify(funkoService, times(1)).create(funkoCreateDto);
    }


    @Test
    void createTestAlreadyExist() throws Exception {
        when(funkoService.create(funkoCreateDto)).thenThrow(new FunkoAlreadyExistException("El funko con el id " + 1 + " ya existe"));

        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class );

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals( HttpStatus.BAD_REQUEST.value(),res.status()),
                () -> assertEquals("El funko con el id " + 1 + " ya existe", res.msg())
        );

        verify(funkoService, times(1)).create(funkoCreateDto);
    }

    @Test
    void createdFunkoWithoutNombre() throws Exception {
        funkoCreateDto.setNombre(null);

        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals( HttpStatus.BAD_REQUEST.value(),res.get("code")),
                () -> assertEquals("El nombre no puede estar vacio", errors.get("nombre"))
        );

        verify(funkoService, times(0)).create(funkoCreateDto);
    }

    @Test
    void createFunkoWithoutPrecio() throws Exception {
        funkoCreateDto.setPrecio(null);

        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("El precio es obligatorio", errors.get("precio"))
        );

        verify(funkoService, times(0)).create(funkoCreateDto);
    }

    @Test
    void createFunkoWithoutCategoria() throws Exception {
        funkoCreateDto.setCategoria(null);

        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("La categoria es obligatoria", errors.get("categoria"))
        );

        verify(funkoService, times(0)).create(funkoCreateDto);
    }

    @Test
    void createFunkoWithoutCantidad() throws Exception {
        funkoCreateDto.setCantidad(null);

        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("La cantidad es obligatoria", errors.get("cantidad"))
        );

        verify(funkoService, times(0)).create(funkoCreateDto);
    }

    @Test
    void createFunkoWithPriceNegative() throws Exception {
        funkoCreateDto.setPrecio(-1.0);

        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("El precio no puede ser negativo", errors.get("precio"))
        );

        verify(funkoService, times(0)).create(funkoCreateDto);
    }

    @Test
    void createFunkoWithCantidadNegative() throws Exception {
        funkoCreateDto.setCantidad(-1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("La cantidad no puede ser negativa", errors.get("cantidad"))
        );

        verify(funkoService, times(0)).create(funkoCreateDto);
    }

    @Test
    void updateFunko() throws Exception {
        when(funkoService.update(1L, funkoCreateDto)).thenReturn(funkoDto);

        MockHttpServletResponse response = mockMvc.perform(
                        put(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        FunkoDto res = mapper.readValue(response.getContentAsString(), FunkoDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(funkoDto.getNombre(), res.getNombre()),
                () -> assertEquals(funkoDto.getId(), res.getId())
        );

        verify(funkoService, times(1)).update(1L, funkoCreateDto);
    }

    @Test
    void updateFunkoNotExist() throws Exception {
        when(funkoService.update(99L, funkoCreateDto)).thenThrow(new FunkoNotFoundException(99L));

        MockHttpServletResponse response = mockMvc.perform(
                        put(endPoint + "/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class );

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), res.status()),
                () -> assertEquals("El funko con id:99 no existe", res.msg())
        );

        verify(funkoService, times(1)).update(99L, funkoCreateDto);
    }

    @Test
    void updateWhitoutNombre() throws Exception {
        funkoCreateDto.setNombre(null);

        MockHttpServletResponse response = mockMvc.perform(
                        put(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("El nombre no puede estar vacio", errors.get("nombre"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void updateWithoutPrecio() throws Exception {
        funkoCreateDto.setPrecio(null);

        MockHttpServletResponse response = mockMvc.perform(
                        put(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("El precio es obligatorio", errors.get("precio"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void updateWithoutCantidad() throws Exception{
        funkoCreateDto.setCantidad(null);

        MockHttpServletResponse response = mockMvc.perform(
                        put(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("La cantidad es obligatoria", errors.get("cantidad"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void updateWithoutCategoria() throws Exception{
        funkoCreateDto.setCategoria(null);

        MockHttpServletResponse response = mockMvc.perform(
                        put(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("La categoria es obligatoria", errors.get("categoria"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void updateWithPrecioNegative() throws Exception{
        funkoCreateDto.setPrecio(-1.0);

        MockHttpServletResponse response = mockMvc.perform(
                        put(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals( HttpStatus.BAD_REQUEST.value(),res.get("code")),
                ()-> assertEquals("El precio no puede ser negativo", errors.get("precio"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void updateWithCantidadNegative() throws Exception {
        funkoCreateDto.setCantidad(-1);

        MockHttpServletResponse response = mockMvc.perform(
                        put(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(),res.get("code")),
                ()-> assertEquals("La cantidad no puede ser negativa",errors.get("cantidad") )
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }


    @Test
    void patchFunko() throws Exception {
        when(funkoService.update(1L, funkoCreateDto)).thenReturn(funkoDto);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        FunkoDto res = mapper.readValue(response.getContentAsString(), FunkoDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(funkoDto.getNombre(), res.getNombre()),
                () -> assertEquals(funkoDto.getId(), res.getId())
        );

        verify(funkoService, times(1)).update(1L, funkoCreateDto);
    }

    @Test
    void patchFunkoNotExist() throws Exception {
        when(funkoService.update(99L, funkoCreateDto)).thenThrow(new FunkoNotFoundException(99L));

        MockHttpServletResponse response = mockMvc.perform(
                        patch(endPoint + "/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class );

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), res.status()),
                () -> assertEquals("El funko con id:99 no existe", res.msg())
        );

        verify(funkoService, times(1)).update(99L, funkoCreateDto);
    }

    @Test
    void patchWithoutNombre() throws Exception {
        funkoCreateDto.setNombre(null);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                ()-> assertEquals("El nombre no puede estar vacio", errors.get("nombre"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void patchWithoutPrecio() throws Exception {
        funkoCreateDto.setPrecio(null);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                ()-> assertEquals("El precio es obligatorio", errors.get("precio"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void patchWithoutCategoria() throws Exception {
        funkoCreateDto.setCategoria(null);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(),res.get("code") ),
                ()-> assertEquals("La categoria es obligatoria", errors.get("categoria"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void patchWithoutCantidad() throws Exception {
        funkoCreateDto.setCantidad(null);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(),res.get("code") ),
                ()-> assertEquals("La cantidad es obligatoria", errors.get("cantidad"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void patchWithPrecioNegative() throws Exception {
        funkoCreateDto.setPrecio(-1.0);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                ()-> assertEquals("El precio no puede ser negativo", errors.get("precio"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void patchWithCantidadNegative() throws Exception {
        funkoCreateDto.setCantidad(-1);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(endPoint + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                ()-> assertEquals("La cantidad no puede ser negativa", errors.get("cantidad"))
        );

        verify(funkoService, times(0)).update(1L, funkoCreateDto);
    }

    @Test
    void deleteById() throws Exception {
        when(funkoService.deleteById(1L)).thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(delete(endPoint + "/1").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(response.getContentAsString(),"El funko con id 1 fue borrado")
        );
    }

    @Test
    void deleteByIdNotExists() throws Exception {
        when(funkoService.deleteById(99L)).thenThrow(new FunkoNotFoundException(99L));

        MockHttpServletResponse response = mockMvc.perform(delete(endPoint + "/99").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class );

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), res.status()),
                () -> assertEquals("El funko con id:99 no existe", res.msg())
        );
    }


}
