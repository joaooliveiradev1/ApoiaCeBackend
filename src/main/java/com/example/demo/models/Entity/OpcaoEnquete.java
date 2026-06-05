package com.example.demo.models.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "opcoes_enquete")
@SQLRestriction("deleted_at IS NULL")
public class OpcaoEnquete {

    @Id
    @Column(name = "id", columnDefinition = "char(36)", nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enquete_id", nullable = false)
    private Enquete enquete;

    @Column(length = 160, nullable = false)
    private String titulo;

    @Column(name = "qtd_votos", nullable = false)
    private int qtdVotos = 0;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    void prePersist() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        this.criadoEm = now;
        this.atualizadoEm = now;
    }

    @PreUpdate
    void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void incrementarVoto() {
        this.qtdVotos++;
    }

    public void decrementarVoto() {
        if (this.qtdVotos > 0) this.qtdVotos--;
    }
}