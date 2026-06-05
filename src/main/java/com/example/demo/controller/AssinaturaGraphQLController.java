package com.example.demo.controller;

import com.example.demo.models.Entity.Assinatura;
import com.example.demo.models.Entity.Projeto;
import com.example.demo.models.Entity.Usuario;
import com.example.demo.models.Enums.AssinaturaStatus;
import com.example.demo.repository.AssinaturaRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class AssinaturaGraphQLController {

    private final AssinaturaRepository assinaturaRepository;

    public AssinaturaGraphQLController(AssinaturaRepository assinaturaRepository) {
        this.assinaturaRepository = assinaturaRepository;
    }

    @QueryMapping
    public List<Assinatura> assinaturas() {
        return assinaturaRepository.findAll();
    }

    @QueryMapping
    public Optional<Assinatura> assinatura(@Argument String id) {
        return assinaturaRepository.findById(id);
    }

    @QueryMapping
    public List<Assinatura> assinaturasPorProjeto(@Argument String projetoId) {
        return assinaturaRepository.findByProjetoId(projetoId);
    }

    @QueryMapping
    public List<Assinatura> assinaturasPorProjetoEStatus(@Argument String projetoId,
                                                         @Argument AssinaturaStatus status) {
        return assinaturaRepository.findByProjetoIdAndStatus(projetoId, status);
    }

    @QueryMapping
    public List<Assinatura> assinaturasPorAssinante(@Argument String assinanteId) {
        return assinaturaRepository.findByAssinanteId(assinanteId);
    }

    @QueryMapping
    public List<Assinatura> assinaturasPorAssinanteEStatus(@Argument String assinanteId,
                                                           @Argument AssinaturaStatus status) {
        return assinaturaRepository.findByAssinanteIdAndStatus(assinanteId, status);
    }

    @QueryMapping
    public String totalApoioAtivoProjeto(@Argument String projetoId) {
        BigDecimal total = assinaturaRepository.sumValorAtivoByProjetoId(projetoId);
        return total != null ? total.toPlainString() : "0";
    }

    @QueryMapping
    public Long totalApoiadoresAtivosProjeto(@Argument String projetoId) {
        return assinaturaRepository.countByProjetoIdAndStatus(projetoId, AssinaturaStatus.ATIVA);
    }

    @SchemaMapping(typeName = "Assinatura", field = "assinante")
    public Usuario assinante(Assinatura assinatura) {
        if (assinatura.isAnonima()) {
            return null;
        }
        return assinatura.getAssinante();
    }

    @SchemaMapping(typeName = "Assinatura", field = "projeto")
    public Projeto projeto(Assinatura assinatura) {
        return assinatura.getProjeto();
    }
}