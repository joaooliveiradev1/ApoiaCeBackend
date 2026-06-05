package com.example.demo.repository;

import com.example.demo.models.Entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, String> {

    List<Notificacao> findByUsuarioIdOrderByCriadoEmDesc(String usuarioId);

    List<Notificacao> findByUsuarioIdAndLidaFalseOrderByCriadoEmDesc(String usuarioId);

    // Contar não lidas (para badge no frontend)
    long countByUsuarioIdAndLidaFalse(String usuarioId);

    @Modifying
    @Query("UPDATE Notificacao n SET n.lida = true, n.atualizadoEm = :agora WHERE n.id = :id AND n.usuarioId = :usuarioId")
    int marcarComoLida(@Param("id") String id,
                       @Param("usuarioId") String usuarioId,
                       @Param("agora") LocalDateTime agora);

    @Modifying
    @Query("UPDATE Notificacao n SET n.lida = true, n.atualizadoEm = :agora WHERE n.usuarioId = :usuarioId AND n.lida = false")
    int marcarTodasComoLidas(@Param("usuarioId") String usuarioId,
                             @Param("agora") LocalDateTime agora);

    @Modifying
    @Query("UPDATE Notificacao n SET n.deletedAt = :agora WHERE n.id = :id AND n.usuarioId = :usuarioId")
    int softDelete(@Param("id") String id,
                   @Param("usuarioId") String usuarioId,
                   @Param("agora") LocalDateTime agora);
}