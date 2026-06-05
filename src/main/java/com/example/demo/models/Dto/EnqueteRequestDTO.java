package com.example.demo.models.Dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class EnqueteRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 150, message = "Título deve ter no máximo 150 caracteres")
    private String titulo;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotEmpty(message = "A enquete precisa ter pelo menos uma opção")
    @Size(min = 2, message = "A enquete precisa ter pelo menos 2 opções")
    @Valid
    private List<OpcaoEnqueteRequestDTO> opcoes;
}