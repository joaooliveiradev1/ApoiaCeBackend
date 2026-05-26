package com.example.demo.models.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpcaoEnqueteRequestDTO {

    @NotBlank(message = "Texto da opção é obrigatório")
    @Size(max = 200, message = "Texto da opção deve ter no máximo 200 caracteres")
    private String texto;
}