package com.example.demo.service;

import com.example.demo.models.Dto.PerfilUsuarioRequestDTO;
import com.example.demo.models.Dto.PerfilUsuarioResponseDTO;
import com.example.demo.models.Entity.PerfilUsuario;
import com.example.demo.models.Entity.Usuario;
import com.example.demo.repository.PerfilUsuarioRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@Transactional
public class PerfilUsuarioService {

    private final PerfilUsuarioRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public PerfilUsuarioService(PerfilUsuarioRepository perfilRepository,
                                UsuarioRepository usuarioRepository) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }


    public PerfilUsuarioResponseDTO criar(String usuarioId, PerfilUsuarioRequestDTO dto) {
        if (perfilRepository.existsByUsuarioIdAndDeletedAtIsNull(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Usuário já possui um perfil ativo");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"));

        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setUsuario(usuario);
        perfil.setBio(dto.getBio());
        perfil.setPixChave(dto.getPixChave());
        perfil.setContaBancaria(dto.getContaBancaria());

        return toResponse(perfilRepository.save(perfil));
    }


    @Transactional(readOnly = true)
    public PerfilUsuarioResponseDTO buscarPorUsuarioId(String usuarioId) {
        PerfilUsuario perfil = perfilRepository
                .findByUsuarioIdAndDeletedAtIsNull(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Perfil não encontrado"));

        return toResponse(perfil);
    }


    public PerfilUsuarioResponseDTO atualizar(String usuarioId, PerfilUsuarioRequestDTO dto) {
        PerfilUsuario perfil = perfilRepository
                .findByUsuarioIdAndDeletedAtIsNull(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Perfil não encontrado"));

        if (dto.getBio() != null) perfil.setBio(dto.getBio());
        if (dto.getPixChave() != null) perfil.setPixChave(dto.getPixChave());
        if (dto.getContaBancaria() != null) perfil.setContaBancaria(dto.getContaBancaria());

        perfil.setAtualizadoEm(LocalDateTime.now());

        return toResponse(perfilRepository.save(perfil));
    }


    public void deletar(String usuarioId) {
        PerfilUsuario perfil = perfilRepository
                .findByUsuarioIdAndDeletedAtIsNull(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Perfil não encontrado"));

        perfil.setDeletedAt(LocalDateTime.now());
        perfilRepository.save(perfil);
    }


    private PerfilUsuarioResponseDTO toResponse(PerfilUsuario perfil) {
        PerfilUsuarioResponseDTO response = new PerfilUsuarioResponseDTO();
        response.setId(perfil.getId());
        response.setUsuarioId(perfil.getUsuario().getId());
        response.setNomeUsuario(perfil.getUsuario().getNome());
        response.setEmail(perfil.getUsuario().getEmail());
        response.setBio(perfil.getBio());
        response.setPixChave(perfil.getPixChave());
        response.setContaBancaria(perfil.getContaBancaria());
        response.setCriadoEm(perfil.getCriadoEm());
        response.setAtualizadoEm(perfil.getAtualizadoEm());
        return response;
    }
}