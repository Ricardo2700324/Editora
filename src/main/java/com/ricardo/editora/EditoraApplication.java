package com.ricardo.editora;

import com.ricardo.editora.entities.Autor;
import com.ricardo.editora.entities.Livro;
import com.ricardo.editora.repositories.AutorRepository;
import com.ricardo.editora.repositories.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class EditoraApplication implements CommandLineRunner {


	@Autowired
	private AutorRepository autorRepsitory;
	@Autowired
	private LivroRepository livroRepsitory;

	public static void main(String[] args) {
		SpringApplication.run(EditoraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Criando autores
		Autor autor1 = new Autor(null, "Robert Martin");
		Autor autor2 = new Autor(null, "Martin Fowler");

		// Criando livros e associando-os a autores
		Livro l1 = new Livro(null, "Clean Code", "Loren ipsun", autor1);
		Livro l2 = new Livro(null, "Refactoring", "Loren ipsun", autor2);

		// Associando livros aos autores
		autor1.getLivros().addAll(Arrays.asList(l1));
		autor2.getLivros().addAll(Arrays.asList(l2));

		// Salvando autores e livros
		this.autorRepsitory.saveAllAndFlush(Arrays.asList(autor1, autor2));
		this.livroRepsitory.saveAll(Arrays.asList(l1, l2));
	}

}