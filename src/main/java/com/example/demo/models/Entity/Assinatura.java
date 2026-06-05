package com.example.demo.models.Entity;

import com.example.demo.models.Enums.AssinaturaStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "assinaturas")
@SQLDelete(sql = "UPDATE assinaturas SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Assinatura {

    @Id
    @Column(name = "id", columnDefinition = "char(36)", nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assinante_id", nullable = false)
    private Usuario assinante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private boolean anonima = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssinaturaStatus status = AssinaturaStatus.ATIVA;

    @Column(nullable = false)
    private boolean recorrente = true;

    private LocalDateTime inicioEm;
    private LocalDateTime canceladaEm;
    private LocalDate proximaCobrancaEm;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    private LocalDateTime atualizadoEm = LocalDateTime.now();
    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}