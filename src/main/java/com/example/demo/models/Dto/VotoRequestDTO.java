package com.example.demo.models.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotoRequestDTO {

    @NotBlank(message = "Id da opção é obrigatório")
    private String opcaoId;
}