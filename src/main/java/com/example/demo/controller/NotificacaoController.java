package com.example.demo.controller;

import com.example.demo.models.Dto.NotificacaoContagemResponse;
import com.example.demo.models.Dto.NotificacaoResponseDTO;
import com.example.demo.models.Entity.Usuario;
import com.example.demo.service.NotificacaoService;
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
    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDTO>> listarTodas(
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(notificacaoService.listarPorUsuario(usuario.getId()));
    }

    // GET /notificacoes/nao-lidas
    @GetMapping("/nao-lidas")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarNaoLidas(
            @AuthenticationPrincipal Usuario usuario) {

        return ResponseEntity.ok(notificacaoService.listarNaoLidas(usuario.getId()));
    }

    // GET /notificacoes/contagem
    @GetMapping("/contagem")
    public ResponseEntity<NotificacaoContagemResponse> contarNaoLidas(
            @AuthenticationPrincipal Usuario usuario) {

        return ResponseEntity.ok(notificacaoService.contarNaoLidas(usuario.getId()));
    }

    // PATCH /notificacoes/{id}/lida
    @PatchMapping("/{id}/lida")
    public ResponseEntity<Void> marcarComoLida(
            @PathVariable String id,
            @AuthenticationPrincipal Usuario usuario) {

        notificacaoService.marcarComoLida(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    // PATCH /notificacoes/lida
    @PatchMapping("/lida")
    public ResponseEntity<Void> marcarTodasComoLidas(
            @AuthenticationPrincipal Usuario usuario) {

        notificacaoService.marcarTodasComoLidas(usuario.getId());
        return ResponseEntity.noContent().build();
    }

    // DELETE /notificacoes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable String id,
            @AuthenticationPrincipal Usuario usuario) {

        notificacaoService.deletar(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }
}