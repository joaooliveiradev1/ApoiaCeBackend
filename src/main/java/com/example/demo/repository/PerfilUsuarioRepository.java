package com.example.demo.repository;

import com.example.demo.models.Entity.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, String> {

    Optional<PerfilUsuario> findByUsuarioId(String usuarioId);

    Optional<PerfilUsuario> findByUsuarioIdAndDeletedAtIsNull(String usuarioId);

    boolean existsByUsuarioId(String usuarioId);

    boolean existsByUsuarioIdAndDeletedAtIsNull(String usuarioId);
}