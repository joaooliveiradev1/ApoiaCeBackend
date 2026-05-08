package com.example.demo.repository;

import com.example.demo.models.Entity.Assinatura;
import com.example.demo.models.Enums.AssinaturaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssinaturaRepository extends JpaRepository<Assinatura, String> {

    // buscas por apoiador

    List<Assinatura> findByAssinanteId(String assinanteId);

    List<Assinatura> findByAssinanteIdAndStatus(String assinanteId, AssinaturaStatus status);

    Optional<Assinatura> findByAssinanteIdAndProjetoId(String assinanteId, String projetoId);


    // buscas por projeto

    List<Assinatura> findByProjetoId(String projetoId);

    List<Assinatura> findByProjetoIdAndStatus(String projetoId, AssinaturaStatus status);

    long countByProjetoIdAndStatus(String projetoId, AssinaturaStatus status);


    // validações

    boolean existsByAssinanteIdAndProjetoIdAndStatus(
            String assinanteId, String projetoId, AssinaturaStatus status);

    boolean existsByAssinanteIdAndProjetoId(String assinanteId, String projetoId);


    // cobranças (implementações futuras)


    @Query("SELECT a FROM Assinatura a WHERE a.status = 'ATIVA' " +
            "AND a.recorrente = true AND a.proximaCobrancaEm <= :hoje")
    List<Assinatura> findAssinaturasParaCobranca(@Param("hoje") LocalDate hoje);

    // Inadimplência

    @Query("SELECT a FROM Assinatura a WHERE a.status = 'ATIVA' " +
            "AND a.recorrente = true AND a.proximaCobrancaEm < :dataLimite")
    List<Assinatura> findAssinaturasVencidas(@Param("dataLimite") LocalDate dataLimite);

    // receita total ativa do projeto

    @Query("SELECT COALESCE(SUM(a.valor), 0) FROM Assinatura a " +
            "WHERE a.projeto.id = :projetoId AND a.status = 'ATIVA'")
    java.math.BigDecimal sumValorAtivoByProjetoId(@Param("projetoId") String projetoId);
}