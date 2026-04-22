package com.example.demo.repository;

import com.example.demo.models.Entity.Projeto;
import com.example.demo.models.Enums.StatusProjeto;
import com.example.demo.models.Enums.TipoAssinatura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

    @Repository
    public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

        Optional<Projeto> findBySlug(String slug);
        boolean existsBySlug(String slug);
        boolean existsByTituloIgnoreCaseAndIdNot(String titulo, Long id);

        Page<Projeto> findByStatus(StatusProjeto status, Pageable pageable);
        long countByStatus(StatusProjeto status);

        Page<Projeto> findByCriadorId(Long criadorId, Pageable pageable);
        Page<Projeto> findByCriadorIdAndStatus(Long criadorId, StatusProjeto status, Pageable pageable);
        long countByCriadorId(Long criadorId);

        Page<Projeto> findByCategoriaId(Long categoriaId, Pageable pageable);
        Page<Projeto> findByStatusAndCategoriaId(StatusProjeto status, Long categoriaId, Pageable pageable);

        Page<Projeto> findByTipoAssinatura(TipoAssinatura tipo, Pageable pageable);
        Page<Projeto> findByStatusAndTipoAssinatura(StatusProjeto status, TipoAssinatura tipo, Pageable pageable);

        Page<Projeto> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
        Page<Projeto> findByStatusAndTituloContainingIgnoreCase(StatusProjeto status, String titulo, Pageable pageable);

        Page<Projeto> findByStatusAndDataFimBetween(StatusProjeto status, LocalDate inicio, LocalDate fim, Pageable pageable);
        Page<Projeto> findByStatusAndDataFimBefore(StatusProjeto status, LocalDate data, Pageable pageable);
    }

