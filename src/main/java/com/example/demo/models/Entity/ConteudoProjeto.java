package com.example.demo.models.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import com.example.demo.models.Enums.TipoConteudo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

@Entity
@Table(name = "conteudos_projeto")
@SQLRestriction("deleted_at IS NULL")
public class ConteudoProjeto {

    @Id
    @Column(columnDefinition = "char(36)") // <-- Mudança aqui
    private String id = UUID.randomUUID().toString();

    @Setter
    @NotNull(message = "Projeto é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_conteudo_projeto"))
    private Projeto projeto;

    @Setter
    @NotNull(message = "Tipo de conteúdo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false,
            columnDefinition = "ENUM('TEXTO','IMAGEM','VIDEO','LINK','ARQUIVO')")
    private TipoConteudo tipo;

    @Setter
    @NotBlank(message = "Conteúdo é obrigatório")
    @Column(name = "conteudo", columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Setter
    @Column(name = "posicao", nullable = false)
    private Integer posicao = 0;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // hooks

    @PrePersist
    private void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        if (this.posicao == null) this.posicao = 0;
    }

    @PreUpdate
    private void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    // soft delete

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return this.deletedAt == null;
    }

    // getters

    public String getId() { return id; }
    public Projeto getProjeto() { return projeto; }
    public TipoConteudo getTipo() { return tipo; }
    public String getConteudo() { return conteudo; }
    public Integer getPosicao() { return posicao; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
}