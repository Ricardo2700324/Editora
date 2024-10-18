package com.ricardo.editora.services;

import java.util.List;
import java.util.Optional;

import com.ricardo.editora.dto.AutorDTO;
import com.ricardo.editora.entities.Autor;
import com.ricardo.editora.repositories.AutorRepository;
import com.ricardo.editora.resources.exceptions.ObjectNotFoundException;
import com.ricardo.editora.resources.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public Autor findById(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Autor não encontrado! Id:" + id));
    }

    public List<Autor> findAll() {
        return autorRepository.findAll();
    }

    public Autor update(Long id, AutorDTO objDto) {
        Autor obj = findById(id);
        obj.setNome(objDto.getNome());
        return autorRepository.save(obj);
    }

    public Autor create(Autor obj) {
        obj.setId(null);
        return autorRepository.save(obj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            autorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Autor não pode ser deletado! Possui livros associados.");

        }

    }

}