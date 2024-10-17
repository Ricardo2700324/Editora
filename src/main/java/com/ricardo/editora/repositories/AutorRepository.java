package com.ricardo.editora.repositories;

import com.ricardo.editora.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {

}
