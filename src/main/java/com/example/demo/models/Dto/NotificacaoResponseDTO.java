package com.example.demo.models.Dto;


import com.example.demo.models.Entity.Notificacao;
import com.example.demo.models.Enums.TipoNotificacao;

import java.time.LocalDateTime;

public record NotificacaoResponseDTO(
        String id,
        String usuarioId,
        String projetoId,
        String titulo,
        String mensagem,
        TipoNotificacao tipo,
        Boolean lida,
        LocalDateTime criadoEm
) {
    public static NotificacaoResponseDTO from(Notificacao notificacao) {
        return new NotificacaoResponseDTO(
                notificacao.getId(),
                notificacao.getUsuarioId(),
                notificacao.getProjetoId(),
                notificacao.getTitulo(),
                notificacao.getMensagem(),
                notificacao.getTipo(),
                notificacao.getLida(),
                notificacao.getCriadoEm()
        );
    }
}