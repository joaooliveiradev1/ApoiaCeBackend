package com.example.demo.controller;

import com.example.demo.models.Entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class UsuarioGraphQLController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioGraphQLController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @QueryMapping
    public List<Usuario> usuarios() {
        return usuarioRepository.findAllByDeletedAtIsNull();
    }

    @QueryMapping
    public Optional<Usuario> usuario(@Argument String id) {
        return usuarioRepository.findByIdAndDeletedAtIsNull(id);
    }

    @QueryMapping
    public List<Usuario> searchUsuarios(@Argument String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCaseAndDeletedAtIsNull(nome);
    }
}