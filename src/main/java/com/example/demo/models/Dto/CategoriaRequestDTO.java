package com.example.demo.models.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 80, message = "Nome deve ter no máximo 80 caracteres")
    private String nome;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Cor deve ser um hex válido ex: #3B82F6")
    private String cor;
}