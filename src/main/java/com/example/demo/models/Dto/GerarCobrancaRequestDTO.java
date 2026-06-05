package com.example.demo.models.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GerarCobrancaRequestDTO {
    @NotBlank(message = "ID da assinatura é obrigatório")
    private String assinaturaId;
}
