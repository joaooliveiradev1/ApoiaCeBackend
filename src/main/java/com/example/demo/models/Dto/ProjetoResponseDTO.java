package com.example.demo.models.Dto;

import com.example.demo.models.Entity.Projeto;
import com.example.demo.models.Enums.StatusProjeto;
import com.example.demo.models.Enums.TipoAssinatura;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ProjetoResponseDTO {

    private Long id;
    private String titulo;
    private String slug;
    private String descricao;
    private String videoUrl;
    private String capaUrl;

    private BigDecimal metaValor;
    private BigDecimal valorCaptado;
    private BigDecimal percentualCaptado;
    private Integer qtdApoiadores;
    private boolean metaAtingida;

    private LocalDate dataFim;
    private boolean encerrado;

    private TipoAssinatura tipoAssinatura;
    private StatusProjeto status;

    private Long criadorId;
    private String criadorNome;

    private Long categoriaId;
    private String categoriaNome;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;


    public static ProjetoResponseDTO from(Projeto p) {
        return ProjetoResponseDTO.builder()
                .id(p.getId())
                .titulo(p.getTitulo())
                .slug(p.getSlug())
                .descricao(p.getDescricao())
                .videoUrl(p.getVideoUrl())
                .capaUrl(p.getCapaUrl())
                .metaValor(p.getMetaValor())
                .valorCaptado(p.getValorCaptado())
                .percentualCaptado(p.percentualCaptado())
                .qtdApoiadores(p.getQtdApoiadores())
                .metaAtingida(p.isMeta())
                .dataFim(p.getDataFim())
                .encerrado(p.isEncerrado())
                .tipoAssinatura(p.getTipoAssinatura())
                .status(p.getStatus())
                .criadorId((p.getCriador().getId()))
                .criadorNome(p.getCriador().getNome())
                .categoriaId(p.getCategoria().getId())
                .categoriaNome(p.getCategoria().getNome())
                .criadoEm(p.getCriadoEm())
                .atualizadoEm(p.getAtualizadoEm())
                .build();
    }
}