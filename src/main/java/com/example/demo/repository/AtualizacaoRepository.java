package com.example.demo.repository;

import com.example.demo.models.Entity.Atualizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtualizacaoRepository extends JpaRepository<Atualizacao, String> {

    List<Atualizacao> findByProjetoIdOrderByPublicadaEmDesc(String projetoId);

    Optional<Atualizacao> findByIdAndProjetoId(String id, String projetoId);

    boolean existsByProjetoIdAndId(String projetoId, String id);


}