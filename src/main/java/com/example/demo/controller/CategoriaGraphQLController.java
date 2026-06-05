package com.example.demo.controller;

import com.example.demo.models.Entity.Categoria;
import com.example.demo.repository.CategoriaRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoriaGraphQLController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaGraphQLController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @QueryMapping
    public List<Categoria> categorias() {
        return categoriaRepository.findAllByDeletedAtIsNull();
    }

    @QueryMapping
    public Optional<Categoria> categoria(@Argument String id) {
        return categoriaRepository.findByIdAndDeletedAtIsNull(id);
    }

    @QueryMapping
    public List<Categoria> searchCategorias(@Argument String nome) {
        return categoriaRepository.findByNomeContainingIgnoreCase(nome);
    }
}