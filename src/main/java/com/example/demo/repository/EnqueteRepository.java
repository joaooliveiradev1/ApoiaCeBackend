package com.example.demo.repository;

import com.example.demo.models.Entity.Enquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnqueteRepository extends JpaRepository<Enquete, String> {

    boolean existsByProjetoId(String projetoId);

    @Query("SELECT e FROM Enquete e LEFT JOIN FETCH e.opcoes WHERE e.id = :id")
    Optional<Enquete> findByIdWithOpcoes(@Param("id") String id);

    @Query("SELECT e FROM Enquete e LEFT JOIN FETCH e.opcoes WHERE e.projeto.id = :projetoId ORDER BY e.dataCriacao DESC")
    List<Enquete> findByProjetoIdWithOpcoes(@Param("projetoId") String projetoId);
}