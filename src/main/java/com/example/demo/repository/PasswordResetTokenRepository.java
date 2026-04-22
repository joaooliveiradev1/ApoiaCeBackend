package com.example.demo.repository;

import com.example.demo.models.Entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    Optional<PasswordResetToken> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByUsuarioId(Long usuarioId);

}