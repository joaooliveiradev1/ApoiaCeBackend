package com.example.demo.controller;

import com.example.demo.models.Entity.Projeto;
import com.example.demo.models.Enums.StatusProjeto;
import com.example.demo.repository.ProjetoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ProjetoGraphQLController {

    @Autowired
    private ProjetoRepository projetoRepository;

    @QueryMapping
    public List<Projeto> projetos() {
        return projetoRepository.findAllBy();
    }

    @QueryMapping
    public Optional<Projeto> projeto(@Argument String id) {
        return projetoRepository.findProjetoWithCategoriaAndCriadorById(id);
    }

    @QueryMapping
    public List<Projeto> projetosPorCategoria(@Argument String categoriaId) {
        return projetoRepository.findByCategoriaId(categoriaId);
    }

    @QueryMapping
    public List<Projeto> projetosPorStatus(@Argument StatusProjeto status) {
        return projetoRepository.findProjetosByStatus(status);
    }


}