package com.example.demo.models.Dto;

import com.example.demo.models.Enums.AssinaturaStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AssinaturaResponseDTO {

    private String id;
    private String assinanteNome;
    private String assinanteId;
    private String projetoId;
    private String projetoTitulo;
    private BigDecimal valor;
    private boolean anonima;
    private AssinaturaStatus status;
    private boolean recorrente;
    private LocalDateTime inicioEm;
    private LocalDateTime canceladaEm;
    private LocalDate proximaCobrancaEm;
    private LocalDateTime criadoEm;
}