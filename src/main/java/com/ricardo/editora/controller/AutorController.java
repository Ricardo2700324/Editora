package com.ricardo.editora.controller;

import com.ricardo.editora.dto.AutorDTO;
import com.ricardo.editora.entities.Autor;
import com.ricardo.editora.resources.exceptions.ObjectNotFoundException;
import com.ricardo.editora.services.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/autores")
public class AutorController {

    @Autowired
    private AutorService service;

    @GetMapping()
    public ResponseEntity<List<AutorDTO>> findAll(){
        List<Autor> list = service.findAll();
        List<AutorDTO> listDTO = list.stream().map(obj -> new AutorDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Autor> findById(@PathVariable Long id) {
        try {
            Autor obj = service.findById(id);
            return ResponseEntity.ok().body(obj);
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<AutorDTO> update(@PathVariable Long id, @RequestBody AutorDTO objDto){
        Autor newObj = service.update(id, objDto);
        return ResponseEntity.ok().body(new AutorDTO(newObj));
    }

    @PostMapping
    public ResponseEntity<Autor> create(@RequestBody Autor obj){
        obj = service.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();

    }

    @DeleteMapping(value= "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

