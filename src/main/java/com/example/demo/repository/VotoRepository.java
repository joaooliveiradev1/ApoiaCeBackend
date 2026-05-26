package com.example.demo.repository;

import com.example.demo.models.Entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotoRepository extends JpaRepository<Voto, String> {

    boolean existsByEnqueteIdAndUsuarioId(String enqueteId, String usuarioId);

    Optional<Voto> findByEnqueteIdAndUsuarioId(String enqueteId, String usuarioId);
}