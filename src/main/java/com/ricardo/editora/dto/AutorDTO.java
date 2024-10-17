package com.ricardo.editora.dto;

import com.ricardo.editora.entities.Autor;

import java.io.Serializable;


public class AutorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;

    public AutorDTO() {
        super();
    }

    public AutorDTO(Autor obj) {
        super();
        this.id = obj.getId();
        this.nome = obj.getNome();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

