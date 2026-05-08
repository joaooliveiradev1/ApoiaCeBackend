package com.example.demo.models.Entity;

import com.example.demo.models.Enums.StatusProjeto;
import com.example.demo.models.Enums.TipoAssinatura;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "projetos")
@SQLRestriction("deleted_at IS NULL")
public class Projeto {

    @Id
    @Column(length = 36)
    private String id = UUID.randomUUID().toString();

    @Setter
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 160, message = "Título deve ter no máximo 160 caracteres")
    @Column(name = "titulo", length = 160, nullable = false)
    private String titulo;

    @Setter
    @Column(name = "slug", length = 180, unique = true, nullable = false)
    private String slug;

    // content

    @Setter
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Setter
    @Column(name = "video_url", columnDefinition = "TEXT")
    private String videoUrl;

    @Setter
    @Column(name = "capa_url", columnDefinition = "TEXT")
    private String capaUrl;

    // financeiro

    @Setter
    @NotNull(message = "Meta de valor é obrigatória")
    @DecimalMin(value = "1.00", message = "Meta deve ser no mínimo R$ 1,00")
    @Digits(integer = 10, fraction = 2, message = "Formato inválido para meta_valor")
    @Column(name = "meta_valor", precision = 12, scale = 2, nullable = false)
    private BigDecimal metaValor;

    @Setter
    @Column(name = "valor_captado", precision = 12, scale = 2, nullable = false)
    private BigDecimal valorCaptado = BigDecimal.ZERO;

    @Setter
    @Column(name = "qtd_apoiadores", nullable = false)
    private Integer qtdApoiadores = 0;

    // validações

    @Setter
    @NotNull(message = "Data de encerramento é obrigatória")
    @Future(message = "Data de encerramento deve ser no futuro")
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Setter
    @NotNull(message = "Tipo de assinatura é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_assinatura", nullable = false,
            columnDefinition = "ENUM('MENSAL','UNICA')")
    private TipoAssinatura tipoAssinatura;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false,
            columnDefinition = "ENUM('RASCUNHO','PUBLICADO','PAUSADO','ENCERRADO')")
    private StatusProjeto status = StatusProjeto.RASCUNHO;

    // relationships

    @Setter
    @NotNull(message = "Criador é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_projeto_criador"))
    private Usuario criador;

    @Setter
    @NotNull(message = "Categoria é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_projeto_categoria"))
    private Categoria categoria;

    // querys de pesquisa

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
        if (this.slug == null || this.slug.isBlank()) {
            this.slug = gerarSlug(this.titulo);
        }
        if (this.valorCaptado == null) {
            this.valorCaptado = BigDecimal.ZERO;
        }
        if (this.qtdApoiadores == null) {
            this.qtdApoiadores = 0;
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    // soft delete

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.status = StatusProjeto.ENCERRADO;
    }

    public boolean isAtivo() {
        return this.deletedAt == null;
    }

    // logica de negocio

    public boolean isMeta() {
        return this.valorCaptado.compareTo(this.metaValor) >= 0;
    }

    public BigDecimal percentualCaptado() {
        if (this.metaValor.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return this.valorCaptado
                .multiply(new BigDecimal("100"))
                .divide(this.metaValor, 2, java.math.RoundingMode.HALF_UP);
    }

    public boolean isEncerrado() {
        return this.dataFim.isBefore(LocalDate.now())
                || this.status == StatusProjeto.ENCERRADO;
    }

    // slug utilitario

    private String gerarSlug(String texto) {
        if (texto == null) return "";
        return texto.toLowerCase()
                .replaceAll("[áàãâä]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[íìîï]", "i")
                .replaceAll("[óòõôö]", "o")
                .replaceAll("[úùûü]", "u")
                .replaceAll("[ç]", "c")
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("[\\s]+", "-")
                .replaceAll("-+", "-");
    }


    public String getId() { return id; }

    public String getTitulo() { return titulo; }

    public String getSlug() { return slug; }

    public String getDescricao() { return descricao; }

    public String getVideoUrl() { return videoUrl; }

    public String getCapaUrl() { return capaUrl; }

    public BigDecimal getMetaValor() { return metaValor; }

    public BigDecimal getValorCaptado() { return valorCaptado; }

    public Integer getQtdApoiadores() { return qtdApoiadores; }

    public LocalDate getDataFim() { return dataFim; }

    public TipoAssinatura getTipoAssinatura() { return tipoAssinatura; }

    public StatusProjeto getStatus() { return status; }

    public Usuario getCriador() { return criador; }

    public Categoria getCategoria() { return categoria; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
}