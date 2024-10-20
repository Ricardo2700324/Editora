package com.ricardo.editora.entities;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;


@Entity(name = "tb_livro")
public class Livro implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Campo titulo requerido!")
    @Length(min = 3, max = 50, message = "O campo titulo deve ter entre 3 e 50 caracteres!")
    private String titulo;

    @NotEmpty(message = "Campo texto requerido!")
    @Length(min = 3, max = 2000000, message = "O campo texto deve ter entre 3 e 2.000.000 caracteres!")
    private String texto;


    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Livro() {
    }

    public Livro(Long id, String titulo, String texto, Autor autor) {
        super();
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Livro other = (Livro) obj;
        return Objects.equals(id, other.id);
    }
}

