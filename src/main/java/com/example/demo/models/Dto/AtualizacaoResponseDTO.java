package com.example.demo.models.Dto;

import com.example.demo.models.Entity.Atualizacao;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AtualizacaoResponseDTO {

    private String id;
    private String projetoId;
    private String titulo;
    private String conteudoPublico;
    private String conteudoExclusivo;
    private boolean exclusiva;
    private LocalDateTime publicadaEm;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public static AtualizacaoResponseDTO from(Atualizacao a) {
        return AtualizacaoResponseDTO.builder()
                .id(a.getId())
                .projetoId(a.getProjeto().getId())
                .titulo(a.getTitulo())
                .conteudoPublico(a.getConteudoPublico())
                .conteudoExclusivo(a.getConteudoExclusivo())
                .exclusiva(a.isExclusiva())
                .publicadaEm(a.getPublicadaEm())
                .criadoEm(a.getCriadoEm())
                .atualizadoEm(a.getAtualizadoEm())
                .build();
    }
}