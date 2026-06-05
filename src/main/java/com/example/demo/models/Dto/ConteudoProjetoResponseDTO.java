package com.example.demo.models.Dto;

import com.example.demo.models.Entity.ConteudoProjeto;
import com.example.demo.models.Enums.TipoConteudo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConteudoProjetoResponseDTO {

    private String id;
    private String projetoId;
    private TipoConteudo tipo;
    private String conteudo;
    private Integer posicao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public static ConteudoProjetoResponseDTO from(ConteudoProjeto c) {
        return ConteudoProjetoResponseDTO.builder()
                .id(c.getId())
                .projetoId(c.getProjeto().getId())
                .tipo(c.getTipo())
                .conteudo(c.getConteudo())
                .posicao(c.getPosicao())
                .criadoEm(c.getCriadoEm())
                .atualizadoEm(c.getAtualizadoEm())
                .build();
    }
}