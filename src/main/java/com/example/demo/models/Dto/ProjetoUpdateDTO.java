package com.example.demo.models.Dto;

import com.example.demo.models.Enums.StatusProjeto;
import com.example.demo.models.Enums.TipoAssinatura;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProjetoUpdateDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 160, message = "Título deve ter no máximo 160 caracteres")
    private String titulo;

    private String descricao;

    @NotNull(message = "Meta de valor é obrigatória")
    @DecimalMin(value = "1.00", message = "Meta deve ser no mínimo R$ 1,00")
    @Digits(integer = 10, fraction = 2, message = "Formato inválido para meta_valor")
    private BigDecimal metaValor;

    @NotNull(message = "Data de encerramento é obrigatória")
    @Future(message = "Data de encerramento deve ser no futuro")
    private LocalDate dataFim;

    @NotNull(message = "Tipo de assinatura é obrigatório")
    private TipoAssinatura tipoAssinatura;

    @NotNull(message = "Categoria é obrigatória")
    private String categoriaId;

    private String videoUrl;
    private String capaUrl;

    // Apenas ADMIN ou o próprio criador podem alterar o status
    private StatusProjeto status;
}