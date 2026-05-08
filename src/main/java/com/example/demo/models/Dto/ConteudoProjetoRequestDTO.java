package com.example.demo.models.Dto;

import com.example.demo.models.Enums.TipoConteudo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConteudoProjetoRequestDTO {

    @NotNull(message = "Tipo de conteúdo é obrigatório")
    private TipoConteudo tipo;

    @NotBlank(message = "Conteúdo é obrigatório")
    private String conteudo;

    @Min(value = 0, message = "Posição deve ser maior ou igual a zero")
    private Integer posicao; // null = insere no final automaticamente
}