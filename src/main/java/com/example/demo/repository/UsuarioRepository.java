package com.example.demo.repository;

import com.example.demo.models.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    List<Usuario> findAllByDeletedAtIsNull();

    Optional<Usuario> findByIdAndDeletedAtIsNull(String id);

    List<Usuario> findByNomeContainingIgnoreCaseAndDeletedAtIsNull(String nome);
}