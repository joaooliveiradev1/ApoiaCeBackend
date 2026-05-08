package com.example.demo.repository;

import com.example.demo.models.Entity.ConteudoProjeto;
import com.example.demo.models.Enums.TipoConteudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConteudoProjetoRepository extends JpaRepository<ConteudoProjeto, String> {

    List<ConteudoProjeto> findByProjetoIdOrderByPosicaoAsc(String projetoId);

    List<ConteudoProjeto> findByProjetoIdAndTipoOrderByPosicaoAsc(String projetoId, TipoConteudo tipo);

    Optional<ConteudoProjeto> findByIdAndProjetoId(String id, String projetoId);

    // busca a maior posição atual do projeto para inserir no final
    @Query("SELECT COALESCE(MAX(c.posicao), -1) FROM ConteudoProjeto c WHERE c.projeto.id = :projetoId")
    Integer findMaxPosicaoByProjetoId(@Param("projetoId") String projetoId);

    // reordena: desloca +1 todos os conteúdos a partir de uma posição
    @Modifying
    @Query("UPDATE ConteudoProjeto c SET c.posicao = c.posicao + 1 WHERE c.projeto.id = :projetoId AND c.posicao >= :posicao")
    void shiftPosicaoParaCima(@Param("projetoId") String projetoId, @Param("posicao") int posicao);
}