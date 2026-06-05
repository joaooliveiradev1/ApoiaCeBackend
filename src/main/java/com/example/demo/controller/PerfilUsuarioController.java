package com.example.demo.controller;

import com.example.demo.models.Dto.PerfilUsuarioRequestDTO;
import com.example.demo.models.Dto.PerfilUsuarioResponseDTO;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.PerfilUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Criar userProfile")
    @PostMapping
    public ResponseEntity<PerfilUsuarioResponseDTO> criar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PerfilUsuarioRequestDTO dto) {

        String usuarioId = extrairId(userDetails);
        PerfilUsuarioResponseDTO response = perfilService.criar(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar userProfile")
    @GetMapping
    public ResponseEntity<PerfilUsuarioResponseDTO> buscar(
            @AuthenticationPrincipal UserDetails userDetails) {

        String usuarioId = extrairId(userDetails);
        return ResponseEntity.ok(perfilService.buscarPorUsuarioId(usuarioId));
    }

    @Operation(summary = "Att userProfile")
    @PatchMapping
    public ResponseEntity<PerfilUsuarioResponseDTO> atualizar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PerfilUsuarioRequestDTO dto) {

        String usuarioId = extrairId(userDetails);
        return ResponseEntity.ok(perfilService.atualizar(usuarioId, dto));
    }

    @Operation(summary = "Deletar userProfile")
    @DeleteMapping
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal UserDetails userDetails) {

        String usuarioId = extrairId(userDetails);
        perfilService.deletar(usuarioId);
        return ResponseEntity.noContent().build();
    }


    private String extrairId(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuário autenticado não encontrado"))
                .getId();
    }
}