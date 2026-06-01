package com.example.demo.models.Entity;

import com.example.demo.models.Enums.MeioPagamento;
import com.example.demo.models.Enums.PagamentoStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "pagamentos")
@SQLRestriction("deleted_at IS NULL")
public class Pagamento {

    @Id
    @Column(name = "id", columnDefinition = "char(36)", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assinatura_id", nullable = false)
    private Assinatura assinatura;

    @Column(name = "gateway_tx_id", length = 120)
    private String gatewayTxId;

    @Column(name = "valor_pago", precision = 12, scale = 2)
    private BigDecimal valorPago;

    @Column(name = "valor_liquido", precision = 12, scale = 2)
    private BigDecimal valorLiquido;

    @Column(name = "taxa_plataforma", precision = 12, scale = 2)
    private BigDecimal taxaPlataforma;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PagamentoStatus status = PagamentoStatus.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "meio_pagamento")
    private MeioPagamento meioPagamento;

    private Integer parcelas = 1;

    private LocalDate competencia;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    private void prePersist() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
    }

    @PreUpdate
    private void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public boolean isPendente() {
        return this.status == PagamentoStatus.PENDENTE;
    }

    public boolean isConfirmado() {
        return this.status == PagamentoStatus.CONFIRMADO;
    }

    public void confirmar() {
        this.status = PagamentoStatus.CONFIRMADO;
        this.dataPagamento = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    public void falhar() {
        this.status = PagamentoStatus.FALHOU;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void estornar() {
        if (this.status != PagamentoStatus.CONFIRMADO) {
            throw new IllegalStateException("Apenas pagamentos confirmados podem ser estornados");
        }
        this.status = PagamentoStatus.ESTORNADO;
        this.atualizadoEm = LocalDateTime.now();
    }
}