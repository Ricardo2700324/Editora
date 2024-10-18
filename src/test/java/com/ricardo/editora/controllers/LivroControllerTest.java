package com.ricardo.editora.controllers;


import com.ricardo.editora.controller.LivroController;
import com.ricardo.editora.dto.LivroDTO;
import com.ricardo.editora.entities.Autor;
import com.ricardo.editora.entities.Livro;
import com.ricardo.editora.services.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LivroControllerTest {

    @InjectMocks
    private LivroController livroController;

    @Mock
    private LivroService livroService;

    private Autor autorMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        autorMock = new Autor(1L, "Robert Martin");
    }

    @Test
    void testFindById() {
        Livro livro = new Livro(1L, "Clean Code", "Texto sobre Clean Code", autorMock);
        when(livroService.findById(1L)).thenReturn(livro);

        ResponseEntity<Livro> response = livroController.finById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(livro, response.getBody());
        verify(livroService, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        List<Livro> livros = List.of(
                new Livro(1L, "Clean Code", "Texto sobre Clean Code", autorMock),
                new Livro(2L, "Refactoring", "Texto sobre Refactoring", new Autor(2L, "Martin Fowler"))
        );
        when(livroService.findAll()).thenReturn(livros);

        ResponseEntity<List<LivroDTO>> response = livroController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(livroService, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        Livro livro = new Livro(1L, "Clean Code", "Texto atualizado sobre Clean Code", autorMock);
        when(livroService.update(eq(1L), any(Livro.class))).thenReturn(livro);

        ResponseEntity<Livro> response = livroController.update(1L, livro);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(livro, response.getBody());
        verify(livroService, times(1)).update(eq(1L), any(Livro.class));
    }

    @Test
    void testCreate() {
        Livro livro = new Livro(null, "Clean Code", "Texto sobre Clean Code", autorMock);
        Livro savedLivro = new Livro(1L, "Clean Code", "Texto sobre Clean Code", autorMock);

        when(livroService.create(any(Livro.class))).thenReturn(savedLivro);

        ResponseEntity<Livro> response = livroController.create(livro);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedLivro, response.getBody());
        verify(livroService, times(1)).create(any(Livro.class));
    }

    @Test
    void testDelete() {
        doNothing().when(livroService).delete(1L);

        ResponseEntity<Void> response = livroController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(livroService, times(1)).delete(1L);
    }
}
