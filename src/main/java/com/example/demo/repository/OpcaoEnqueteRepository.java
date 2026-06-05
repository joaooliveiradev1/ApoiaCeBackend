package com.example.demo.repository;

import com.example.demo.models.Entity.OpcaoEnquete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OpcaoEnqueteRepository extends JpaRepository<OpcaoEnquete, String> {

    List<OpcaoEnquete> findByEnqueteIdOrderByCriadoEmAsc(String enqueteId);

    Optional<OpcaoEnquete> findByIdAndEnqueteId(String id, String enqueteId);

    boolean existsByIdAndEnqueteId(String id, String enqueteId);
}