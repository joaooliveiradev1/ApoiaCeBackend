package com.example.demo.controller;

import com.example.demo.models.Dto.NotificacaoContagemResponse;
import com.example.demo.models.Dto.NotificacaoResponseDTO;
import com.example.demo.models.Entity.Usuario;
import com.example.demo.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    // GET /notificacoes
    @Operation(summary = "Listar todas notificacoes de um usuario autenticado")
    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDTO>> listarTodas(
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(notificacaoService.listarPorUsuario(usuario.getId()));
    }

    // GET /notificacoes/nao-lidas
    @Operation(summary = "Listar todas notificacoes nao lidas de um usuario autenticado")
    @GetMapping("/nao-lidas")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarNaoLidas(
            @AuthenticationPrincipal Usuario usuario) {

        return ResponseEntity.ok(notificacaoService.listarNaoLidas(usuario.getId()));
    }

    // GET /notificacoes/contagem
    @Operation(summary = "Contar notificacoes não lidas")
    public ResponseEntity<NotificacaoContagemResponse> contarNaoLidas(
            @AuthenticationPrincipal Usuario usuario) {

        return ResponseEntity.ok(notificacaoService.contarNaoLidas(usuario.getId()));
    }

    // PATCH /notificacoes/{id}/lida
    @Operation(summary = "Marcar notificacao como lida")
    @PatchMapping("/{id}/lida")
    public ResponseEntity<Void> marcarComoLida(
            @PathVariable String id,
            @AuthenticationPrincipal Usuario usuario) {

        notificacaoService.marcarComoLida(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    // PATCH /notificacoes/lida
    @Operation(summary = "Marcar todas as notificacoes como lidas")
    @PatchMapping("/lida")
    public ResponseEntity<Void> marcarTodasComoLidas(
            @AuthenticationPrincipal Usuario usuario) {

        notificacaoService.marcarTodasComoLidas(usuario.getId());
        return ResponseEntity.noContent().build();
    }

    // DELETE /notificacoes/{id}
    @Operation(summary = "Deletar notificacao")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable String id,
            @AuthenticationPrincipal Usuario usuario) {

        notificacaoService.deletar(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }
}