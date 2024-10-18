package com.ricardo.editora.services;

import java.util.List;
import java.util.Optional;

import com.ricardo.editora.entities.Autor;
import com.ricardo.editora.entities.Livro;
import com.ricardo.editora.repositories.AutorRepository;
import com.ricardo.editora.repositories.LivroRepository;
import com.ricardo.editora.resources.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorService autorService;

    public Livro findById(Long id) {
        Optional<Livro> obj = livroRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Livro não encontrado! Id: " + id ));
    }

    public List<Livro> findAll() {
        return livroRepository.findAll();
    }

    public Livro update(Long id, Livro obj) {
        Livro newObj = findById(id);
        updateData(newObj, obj);
        return livroRepository.save(newObj);
    }

    private void updateData(Livro newObj, Livro obj) {
        newObj.setTitulo(obj.getTitulo());
        newObj.setTexto(obj.getTexto());


        if (obj.getAutor() != null && obj.getAutor().getId() != null) {
            Autor autor = autorRepository.findById(obj.getAutor().getId())
                    .orElseThrow(() -> new ObjectNotFoundException("Autor não encontrado! Id: " + obj.getAutor().getId()));
            newObj.setAutor(autor);
        }
    }

    public Livro create(Livro obj) {
        obj.setId(null);
        Autor autor = autorRepository.findById(obj.getAutor().getId())
                .orElseThrow(() -> new ObjectNotFoundException("Autor não encontrado! Id: " + obj.getAutor().getId()));
        obj.setAutor(autor);
        return livroRepository.save(obj);
    }

    public void delete(Long id) {
        Livro obj = findById(id);
        livroRepository.delete(obj);
    }

}
