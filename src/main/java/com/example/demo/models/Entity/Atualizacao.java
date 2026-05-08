package com.example.demo.models.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "atualizacoes")
@Data
@Builder
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@AllArgsConstructor
public class Atualizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Column(nullable = false, length = 160)
    private String titulo;

    @Column(name = "conteudo_publico", columnDefinition = "TEXT")
    private String conteudoPublico;

    @Column(name = "conteudo_exclusivo", columnDefinition = "TEXT")
    private String conteudoExclusivo;

    @Column(nullable = false)
    private boolean exclusiva = false;

    @Column(name = "publicada_em")
    private LocalDateTime publicadaEm;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // soft delete
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return this.deletedAt == null;
    }

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}