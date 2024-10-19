package com.ricardo.editora.services;

import com.ricardo.editora.entities.Autor;
import com.ricardo.editora.entities.Livro;
import com.ricardo.editora.repositories.AutorRepository;
import com.ricardo.editora.repositories.LivroRepository;
import com.ricardo.editora.resources.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class LivroServiceTest {

    @InjectMocks
    private LivroService livroService;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    private Livro livro;
    private Autor autor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        autor = new Autor(1L, "Martin Fowler");
        livro = new Livro(1L, "Refactoring", "Texto sobre refatoração", autor);
    }

    @Test
    void testFindByIdSuccess() {
        when(livroRepository.findById(anyLong())).thenReturn(Optional.of(livro));

        Livro result = livroService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Refactoring", result.getTitulo());
        assertEquals("Martin Fowler", result.getAutor().getNome());
    }

    @Test
    void testFindByIdThrowsObjectNotFoundException() {
        when(livroRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            livroService.findById(1L);
        });

        String expectedMessage = "Livro não encontrado! Id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testFindAll() {
        when(livroRepository.findAll()).thenReturn(Arrays.asList(livro));

        List<Livro> result = livroService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Refactoring", result.get(0).getTitulo());
    }

    @Test
    void testUpdateSuccess() {
        Livro updatedLivro = new Livro(1L, "Clean Code", "Texto sobre Clean Code", autor);
        when(livroRepository.findById(anyLong())).thenReturn(Optional.of(livro));
        when(autorRepository.findById(anyLong())).thenReturn(Optional.of(autor));
        when(livroRepository.save(any(Livro.class))).thenReturn(updatedLivro);

        Livro result = livroService.update(1L, updatedLivro);

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitulo());
        assertEquals("Texto sobre Clean Code", result.getTexto());
        assertEquals("Martin Fowler", result.getAutor().getNome());
    }

    @Test
    void testUpdateWithNonExistentAutor() {
        Livro updatedLivro = new Livro(1L, "Clean Code", "Texto sobre Clean Code", new Autor(99L, null));

        when(livroRepository.findById(anyLong())).thenReturn(Optional.of(livro));
        when(autorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            livroService.update(1L, updatedLivro);
        });

        String expectedMessage = "Autor não encontrado! Id: 99";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreateSuccess() {
        Livro newLivro = new Livro(null, "Clean Architecture", "Texto sobre arquitetura limpa", autor);

        when(autorRepository.findById(anyLong())).thenReturn(Optional.of(autor));
        when(livroRepository.save(any(Livro.class))).thenReturn(newLivro);

        Livro result = livroService.create(newLivro);

        assertNotNull(result);
        assertEquals("Clean Architecture", result.getTitulo());
        assertEquals("Martin Fowler", result.getAutor().getNome());
    }

    @Test
    void testCreateWithNonExistentAutor() {
        Livro newLivro = new Livro(null, "Clean Architecture", "Texto sobre arquitetura limpa", new Autor(99L, null));

        when(autorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            livroService.create(newLivro);
        });

        String expectedMessage = "Autor não encontrado! Id: 99";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeleteSuccess() {
        when(livroRepository.findById(anyLong())).thenReturn(Optional.of(livro));
        doNothing().when(livroRepository).delete(any(Livro.class));

        assertDoesNotThrow(() -> livroService.delete(1L));

        verify(livroRepository, times(1)).delete(any(Livro.class));
    }

    @Test
    void testDeleteThrowsObjectNotFoundException() {
        when(livroRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            livroService.delete(1L);
        });

        String expectedMessage = "Livro não encontrado! Id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

