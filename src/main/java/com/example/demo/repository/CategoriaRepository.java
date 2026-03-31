package com.example.demo.repository;

import com.example.demo.models.Entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findAllByDeletedAtIsNull();

    Optional<Categoria> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByNomeIgnoreCaseAndDeletedAtIsNull(String nome);

    boolean existsByNomeIgnoreCaseAndDeletedAtIsNullAndIdNot(String nome, Long id);

    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND c.deletedAt IS NULL")
    List<Categoria> findByNomeContainingIgnoreCase(String nome);
}