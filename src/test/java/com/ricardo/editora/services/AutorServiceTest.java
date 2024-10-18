package com.ricardo.editora.services;


import com.ricardo.editora.dto.AutorDTO;
import com.ricardo.editora.entities.Autor;
import com.ricardo.editora.repositories.AutorRepository;
import com.ricardo.editora.resources.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AutorServiceTest {

    @InjectMocks
    private AutorService autorService;

    @Mock
    private AutorRepository autorRepository;

    private Autor autor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        autor = new Autor(1L, "Robert Martin");
    }

    @Test
    void testFindByIdSuccess() {
        when(autorRepository.findById(anyLong())).thenReturn(Optional.of(autor));

        Autor result = autorService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Robert Martin", result.getNome());
    }

    @Test
    void testFindByIdThrowsObjectNotFoundException() {
        when(autorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            autorService.findById(1L);
        });

        String expectedMessage = "Autor não encontrado! Id:1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testFindAll() {
        when(autorRepository.findAll()).thenReturn(Arrays.asList(autor));

        List<Autor> result = autorService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Robert Martin", result.get(0).getNome());
    }

    @Test
    void testCreateAutor() {
        Autor newAutor = new Autor(null, "Kent Beck");
        when(autorRepository.save(any(Autor.class))).thenReturn(newAutor);

        Autor createdAutor = autorService.create(newAutor);

        assertNotNull(createdAutor);
        assertEquals("Kent Beck", createdAutor.getNome());
    }

    @Test
    void testUpdateAutor() {
        AutorDTO autorDTO = new AutorDTO(autor);
        when(autorRepository.findById(anyLong())).thenReturn(Optional.of(autor));
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);

        Autor updatedAutor = autorService.update(1L, autorDTO);

        assertNotNull(updatedAutor);
        assertEquals("Robert Martin", updatedAutor.getNome());  // O nome não mudou nesse caso
    }

    @Test
    void testDeleteAutorSuccess() {
        when(autorRepository.findById(anyLong())).thenReturn(Optional.of(autor));
        doNothing().when(autorRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> autorService.delete(1L));

        verify(autorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAutorThrowsDataIntegrityViolationException() {
        when(autorRepository.findById(anyLong())).thenReturn(Optional.of(autor));
        doThrow(new DataIntegrityViolationException("Autor não pode ser deletado! Possui livros associados."))
                .when(autorRepository).deleteById(anyLong());

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            autorService.delete(1L);
        });

        String expectedMessage = "Autor não pode ser deletado! Possui livros associados.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
