package com.example.demo.models.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EnqueteResponseDTO {

    private String id;
    private String titulo;
    private String descricao;
    private String projetoId;
    private LocalDateTime dataCriacao;
    private List<OpcaoEnqueteResponseDTO> opcoes;
}