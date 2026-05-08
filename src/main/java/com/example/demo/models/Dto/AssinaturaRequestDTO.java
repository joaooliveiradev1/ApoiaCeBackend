package com.example.demo.models.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AssinaturaRequestDTO {

    @NotNull(message = "Projeto é obrigatório")
    private String projetoId;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "1.00", message = "Valor mínimo é R$ 1,00")
    private BigDecimal valor;

    private boolean anonima = false;
    private boolean recorrente = true;
}