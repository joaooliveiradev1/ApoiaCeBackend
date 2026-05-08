package com.example.demo.models.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AtualizacaoRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 160, message = "Título deve ter no máximo 160 caracteres")
    private String titulo;

    private String conteudoPublico;

    private String conteudoExclusivo;

    private boolean exclusiva = false;

    private LocalDateTime publicadaEm;
}