package com.example.demo.controller;

import com.example.demo.models.Dto.PerfilUsuarioRequestDTO;
import com.example.demo.models.Dto.PerfilUsuarioResponseDTO;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.PerfilUsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/perfil")
public class PerfilUsuarioController {

    private final PerfilUsuarioService perfilService;
    private final UsuarioRepository usuarioRepository;

    public PerfilUsuarioController(PerfilUsuarioService perfilService, UsuarioRepository usuarioRepository) {
        this.perfilService = perfilService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<PerfilUsuarioResponseDTO> criar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PerfilUsuarioRequestDTO dto) {

        Long usuarioId = extrairId(userDetails);
        PerfilUsuarioResponseDTO response = perfilService.criar(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PerfilUsuarioResponseDTO> buscar(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = extrairId(userDetails);
        return ResponseEntity.ok(perfilService.buscarPorUsuarioId(usuarioId));
    }

    @PatchMapping
    public ResponseEntity<PerfilUsuarioResponseDTO> atualizar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PerfilUsuarioRequestDTO dto) {

        Long usuarioId = extrairId(userDetails);
        return ResponseEntity.ok(perfilService.atualizar(usuarioId, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = extrairId(userDetails);
        perfilService.deletar(usuarioId);
        return ResponseEntity.noContent().build();
    }


    private Long extrairId(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuário autenticado não encontrado"))
                .getId();
    }
}