package com.ricardo.editora.controllers;

import com.ricardo.editora.controller.AutorController;
import com.ricardo.editora.dto.AutorDTO;
import com.ricardo.editora.entities.Autor;
import com.ricardo.editora.resources.exceptions.ObjectNotFoundException;
import com.ricardo.editora.services.AutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutorControllerTest {

    @Mock
    private AutorService service;

    @InjectMocks
    private AutorController controller;

    private Autor autor1;
    private Autor autor2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        autor1 = new Autor(1L, "Robert Martin");
        autor2 = new Autor(2L, "Martin Fowler");
    }

    @Test
    void testFindAll() {
        when(service.findAll()).thenReturn(Arrays.asList(autor1, autor2));

        ResponseEntity<List<AutorDTO>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        when(service.findById(1L)).thenReturn(autor1);

        ResponseEntity<Autor> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Robert Martin", response.getBody().getNome());
        verify(service, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {

        when(service.findById(3L)).thenThrow(new ObjectNotFoundException("Autor não encontrado! Id: 3"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.findById(3L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(service, times(1)).findById(3L);
    }


    @Test
    void testUpdateAutor() {
        AutorDTO autorDTO = new AutorDTO(autor1);
        when(service.update(anyLong(), any(AutorDTO.class))).thenReturn(autor1);

        ResponseEntity<AutorDTO> response = controller.update(1L, autorDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Robert Martin", response.getBody().getNome());
        verify(service, times(1)).update(anyLong(), any(AutorDTO.class));
    }

    @Test
    void testDeleteAutor() {
        doNothing().when(service).delete(1L);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).delete(1L);
    }
}
