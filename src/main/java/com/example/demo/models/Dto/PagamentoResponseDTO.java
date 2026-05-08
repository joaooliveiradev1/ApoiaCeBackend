package com.example.demo.models.Dto;

import com.example.demo.client.AbacatePayClient;
import com.example.demo.models.Entity.Pagamento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// PagamentoResponseDTO.java
@Data
public class PagamentoResponseDTO {
    private String id;
    private String status;
    private BigDecimal valorPago;
    private String meioPagamento;
    private LocalDate competencia;
    private LocalDateTime dataPagamento;
    private LocalDateTime criadoEm;

    // Campos só presentes na geração da cobrança
    private String qrCode;       // brCode copia-e-cola
    private String qrCodeBase64; // imagem para exibir no frontend

    public static PagamentoResponseDTO fromEntity(Pagamento p) {
        PagamentoResponseDTO dto = new PagamentoResponseDTO();
        dto.setId(p.getId());
        dto.setStatus(p.getStatus().name());
        dto.setValorPago(p.getValorPago());
        dto.setMeioPagamento(p.getMeioPagamento() != null ? p.getMeioPagamento().name() : null);
        dto.setCompetencia(p.getCompetencia());
        dto.setDataPagamento(p.getDataPagamento());
        dto.setCriadoEm(p.getCriadoEm());
        return dto;
    }

    public static PagamentoResponseDTO fromCobranca(Pagamento p, AbacatePayClient.CobrancaResponse g) {
        PagamentoResponseDTO dto = fromEntity(p);
        dto.setQrCode(g.getQrCode());
        dto.setQrCodeBase64(g.getQrCodeBase64());
        return dto;
    }
}