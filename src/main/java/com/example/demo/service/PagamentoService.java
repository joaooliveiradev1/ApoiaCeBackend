package com.example.demo.service;

import com.example.demo.client.AbacatePayClient;
import com.example.demo.exception.BusinessException;
import com.example.demo.models.Dto.PagamentoResponseDTO;
import com.example.demo.models.Dto.WebhookPayloadDTO;
import com.example.demo.models.Entity.Assinatura;
import com.example.demo.models.Entity.Pagamento;
import com.example.demo.models.Enums.MeioPagamento;
import com.example.demo.models.Enums.PagamentoStatus;
import com.example.demo.repository.AssinaturaRepository;
import com.example.demo.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AssinaturaRepository assinaturaRepository;
    private final AbacatePayClient abacatePayClient;

    // Gerar cobrança PIX

    @Transactional
    public PagamentoResponseDTO gerarCobranca(String assinaturaId) {
        Assinatura assinatura = assinaturaRepository.findById(assinaturaId)
                .orElseThrow(() -> new BusinessException("Assinatura não encontrada"));

        validarAssinaturaAtiva(assinatura);
        validarSemCobrancaPendente(assinaturaId);

        LocalDate competencia = LocalDate.now().withDayOfMonth(1);
        AbacatePayClient.CobrancaResponse gateway =
                abacatePayClient.criarCobrancaPix(assinatura.getValor(), assinatura.getId());

        BigDecimal taxa = BigDecimal.ZERO;
        BigDecimal liquido = assinatura.getValor().subtract(taxa);

        Pagamento pagamento = new Pagamento();
        pagamento.setAssinatura(assinatura);
        pagamento.setGatewayTxId(gateway.getTxId());
        pagamento.setValorPago(assinatura.getValor());
        pagamento.setTaxaPlataforma(taxa);
        pagamento.setValorLiquido(liquido);
        pagamento.setMeioPagamento(MeioPagamento.PIX);
        pagamento.setStatus(PagamentoStatus.PENDENTE);
        pagamento.setCompetencia(competencia);
        pagamento.setParcelas(1);

        pagamentoRepository.save(pagamento);

        log.info("Cobrança PIX gerada | assinatura={} competencia={} txId={}",
                assinaturaId, competencia, gateway.getTxId());

        return PagamentoResponseDTO.fromCobranca(pagamento, gateway);
    }

    // Processar webhook

    @Transactional
    public void processarWebhook(WebhookPayloadDTO payload) {
        Pagamento pagamento = pagamentoRepository.findByGatewayTxId(payload.getTxId())
                .orElseThrow(() -> {
                    log.warn("Webhook recebido para txId desconhecido: {}", payload.getTxId());
                    return new BusinessException("Pagamento não encontrado para txId: " + payload.getTxId());
                });

        if (!pagamento.isPendente()) {
            log.warn("Webhook ignorado — pagamento já processado | txId={} status={}",
                    payload.getTxId(), pagamento.getStatus());
            return;
        }

        switch (payload.getStatus()) {
            case "PAID"   -> confirmarPagamento(pagamento);  // era "CONFIRMED"
            case "FAILED" -> pagamento.falhar();
            default -> log.warn("Status desconhecido recebido no webhook: {}", payload.getStatus());
        }

        pagamentoRepository.save(pagamento);
    }

    // Consultas

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarPorAssinatura(String assinaturaId) {
        return pagamentoRepository
                .findByAssinaturaIdOrderByCriadoEmDesc(assinaturaId)
                .stream()
                .map(PagamentoResponseDTO::fromEntity)
                .toList();
    }

    // Expiração de cobranças antigas (chamado pelo Scheduler)

    @Transactional
    public void expirarCobrancasAntigas() {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(60);
        List<Pagamento> expirados = pagamentoRepository
                .findByStatusAndCriadoEmBefore(PagamentoStatus.PENDENTE, limite);

        expirados.forEach(p -> {
            p.setStatus(PagamentoStatus.EXPIRADO);
            log.info("Cobrança expirada | id={} assinatura={}", p.getId(), p.getAssinatura().getId());
        });

        pagamentoRepository.saveAll(expirados);
    }

    // Métodos privados

    private void confirmarPagamento(Pagamento pagamento) {
        pagamento.confirmar();

        Assinatura assinatura = pagamento.getAssinatura();
        assinatura.setProximaCobrancaEm(
                assinatura.getProximaCobrancaEm().plusMonths(1));

        assinaturaRepository.save(assinatura);

        log.info("Pagamento confirmado | id={} assinatura={} proximaCobranca={}",
                pagamento.getId(),
                assinatura.getId(),
                assinatura.getProximaCobrancaEm());
    }

    private void validarAssinaturaAtiva(Assinatura assinatura) {
        if (assinatura.getCanceladaEm() != null) {
            throw new BusinessException("Não é possível cobrar uma assinatura cancelada");
        }
    }

    private void validarSemCobrancaPendente(String assinaturaId) {
        LocalDate competencia = LocalDate.now().withDayOfMonth(1);
        boolean jaExiste = pagamentoRepository.existsByAssinaturaIdAndStatusAndCompetencia(
                assinaturaId, PagamentoStatus.PENDENTE, competencia);

        if (jaExiste) {
            throw new BusinessException("Já existe uma cobrança pendente para este mês");
        }
    }
}