package com.example.demo.repository;

import com.example.demo.models.Entity.Pagamento;
import com.example.demo.models.Enums.PagamentoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, String> {

    // Webhook
    // Busca pelo ID da transação retornado pelo gateway — usado no webhook
    Optional<Pagamento> findByGatewayTxId(String gatewayTxId);

    // Histórico por assinatura
    List<Pagamento> findByAssinaturaIdOrderByCriadoEmDesc(String assinaturaId);

    List<Pagamento> findByAssinaturaIdAndStatus(String assinaturaId, PagamentoStatus status);

    // Validação de duplicata
    // Impede gerar nova cobrança se já existe uma pendente para a mesma competência
    boolean existsByAssinaturaIdAndStatusAndCompetencia(
            String assinaturaId, PagamentoStatus status, LocalDate competencia);

    // Conciliação financeira
    // Total confirmado de uma assinatura — útil para relatórios do criador
    @Query("SELECT COALESCE(SUM(p.valorPago), 0) FROM Pagamento p " +
            "WHERE p.assinatura.id = :assinaturaId AND p.status = 'CONFIRMADO'")
    java.math.BigDecimal sumValorConfirmadoByAssinaturaId(@Param("assinaturaId") String assinaturaId);

    List<Pagamento> findByStatusAndCriadoEmBefore(PagamentoStatus status, LocalDateTime limite);

    // Total confirmado de um projeto inteiro em um período
    @Query("SELECT COALESCE(SUM(p.valorLiquido), 0) FROM Pagamento p " +
            "WHERE p.assinatura.projeto.id = :projetoId " +
            "AND p.status = 'CONFIRMADO' " +
            "AND p.competencia BETWEEN :inicio AND :fim")
    java.math.BigDecimal sumValorLiquidoByProjetoIdAndPeriodo(
            @Param("projetoId") String projetoId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    // Cobranças pendentes antigas (scheduler no futuro, talvez)
    @Query("SELECT p FROM Pagamento p WHERE p.status = 'PENDENTE' " +
            "AND p.criadoEm < :limite")
    List<Pagamento> findPagamentosPendentesExpirados(
            @Param("limite") java.time.LocalDateTime limite);
}