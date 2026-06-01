package com.example.demo.controller;

import com.example.demo.models.Dto.UsuarioResponseDTO;
import com.example.demo.models.Dto.UsuarioUpdateRequest;
import com.example.demo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Buscar usuario by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable String id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id).toResponseDTO());
    }

    @Operation(summary = "Att user")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable String id,
            @RequestBody UsuarioUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        var usuario = usuarioService.buscarPorId(id);

        //só o próprio usuário é capaz de atualizar seus dados
        if (!usuario.getEmail().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(usuarioService.atualizar(id, request).toResponseDTO());
    }

    @Operation(summary = "Deletar user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {

        var usuario = usuarioService.buscarPorId(id);

        if (!usuario.getEmail().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).build();
        }

        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
