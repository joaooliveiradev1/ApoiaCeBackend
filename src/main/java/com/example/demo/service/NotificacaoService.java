package com.example.demo.service;


import com.example.demo.models.Dto.NotificacaoContagemResponse;
import com.example.demo.models.Dto.NotificacaoResponseDTO;
import com.example.demo.models.Entity.Notificacao;
import com.example.demo.models.Enums.TipoNotificacao;
import com.example.demo.repository.NotificacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    @Transactional
    public Notificacao criar(String usuarioId, String projetoId,
                             String titulo, String mensagem,
                             TipoNotificacao tipo) {
        Notificacao notificacao = Notificacao.builder()
                .usuarioId(usuarioId)
                .projetoId(projetoId)
                .titulo(titulo)
                .mensagem(mensagem)
                .tipo(tipo)
                .build();

        return notificacaoRepository.save(notificacao);
    }

    public List<NotificacaoResponseDTO> listarPorUsuario(String usuarioId) {
        return notificacaoRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId)
                .stream()
                .map(NotificacaoResponseDTO::from)
                .toList();
    }

    public List<NotificacaoResponseDTO> listarNaoLidas(String usuarioId) {
        return notificacaoRepository.findByUsuarioIdAndLidaFalseOrderByCriadoEmDesc(usuarioId)
                .stream()
                .map(NotificacaoResponseDTO::from)
                .toList();
    }

    public NotificacaoContagemResponse contarNaoLidas(String usuarioId) {
        long total = notificacaoRepository.countByUsuarioIdAndLidaFalse(usuarioId);
        return new NotificacaoContagemResponse(total);
    }

    @Transactional
    public void marcarComoLida(String id, String usuarioId) {
        int atualizadas = notificacaoRepository.marcarComoLida(id, usuarioId, LocalDateTime.now());
        if (atualizadas == 0) {
            throw new EntityNotFoundException("Notificação não encontrada ou não pertence ao usuário.");
        }
    }

    @Transactional
    public void marcarTodasComoLidas(String usuarioId) {
        notificacaoRepository.marcarTodasComoLidas(usuarioId, LocalDateTime.now());
    }

    @Transactional
    public void deletar(String id, String usuarioId) {
        int deletadas = notificacaoRepository.softDelete(id, usuarioId, LocalDateTime.now());
        if (deletadas == 0) {
            throw new EntityNotFoundException("Notificação não encontrada ou não pertence ao usuário.");
        }
    }
}