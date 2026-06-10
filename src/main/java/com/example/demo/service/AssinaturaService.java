package com.example.demo.service;

import com.example.demo.models.Dto.AssinaturaRequestDTO;
import com.example.demo.models.Dto.AssinaturaResponseDTO;
import com.example.demo.models.Entity.Assinatura;
import com.example.demo.models.Entity.Projeto;
import com.example.demo.models.Entity.Usuario;
import com.example.demo.models.Enums.AssinaturaStatus;
import com.example.demo.repository.AssinaturaRepository;
import com.example.demo.repository.ProjetoRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProjetoRepository projetoRepository;

    public AssinaturaService(AssinaturaRepository assinaturaRepository,
                             UsuarioRepository usuarioRepository,
                             ProjetoRepository projetoRepository) {
        this.assinaturaRepository = assinaturaRepository;
        this.usuarioRepository = usuarioRepository;
        this.projetoRepository = projetoRepository;
    }

    //create

    @Transactional
    public AssinaturaResponseDTO assinar(String emailAssinante, AssinaturaRequestDTO dto) {
        Usuario assinante = findUsuarioOrThrow(emailAssinante);
        Projeto projeto = findProjetoOrThrow(dto.getProjetoId());

        validarNaoAssinaProprioProject(assinante, projeto);

        Assinatura assinatura = new Assinatura();
        assinatura.setAssinante(assinante);
        assinatura.setProjeto(projeto);
        assinatura.setValor(dto.getValor());
        assinatura.setAnonima(dto.isAnonima());
        assinatura.setRecorrente(dto.isRecorrente());
        assinatura.setStatus(AssinaturaStatus.ATIVA);
        assinatura.setInicioEm(LocalDateTime.now());
        assinatura.setProximaCobrancaEm(LocalDate.now().plusMonths(1));

        return toDTO(assinaturaRepository.save(assinatura));
    }

    //cancel

    @Transactional
    public AssinaturaResponseDTO cancelar(String assinaturaId, String emailSolicitante) {
        Assinatura assinatura = findAssinaturaOrThrow(assinaturaId);

        validarPropriedade(assinatura, emailSolicitante);
        validarStatusAtivo(assinatura);

        assinatura.setStatus(AssinaturaStatus.CANCELADA);
        assinatura.setCanceladaEm(LocalDateTime.now());

        return toDTO(assinaturaRepository.save(assinatura));
    }

    // querys

    public List<AssinaturaResponseDTO> listarMinhasAssinaturas(String email) {
        Usuario usuario = findUsuarioOrThrow(email);
        return assinaturaRepository
                .findByAssinanteIdAndStatus(usuario.getId(), AssinaturaStatus.ATIVA)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<AssinaturaResponseDTO> listarAssinantesDoProjeto(String projetoId) {
        findProjetoOrThrow(projetoId); // garante que o projeto existe
        return assinaturaRepository
                .findByProjetoIdAndStatus(projetoId, AssinaturaStatus.ATIVA)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public AssinaturaResponseDTO buscarPorId(String assinaturaId, String emailSolicitante) {
        Assinatura assinatura = findAssinaturaOrThrow(assinaturaId);
        validarPropriedade(assinatura, emailSolicitante);
        return toDTO(assinatura);
    }

    public long contarAssinantesAtivos(String projetoId) {
        return assinaturaRepository.countByProjetoIdAndStatus(projetoId, AssinaturaStatus.ATIVA);
    }

    public BigDecimal receitaAtivaDoProjeto(String projetoId) {
        return assinaturaRepository.sumValorAtivoByProjetoId(projetoId);
    }

    // validações

    private void validarNaoAssinaProprioProject(Usuario assinante, Projeto projeto) {
        if (projeto.getCriador().getId().equals(assinante.getId())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "O criador não pode assinar o próprio projeto");
        }
    }

    private void validarPropriedade(Assinatura assinatura, String emailSolicitante) {
        if (!assinatura.getAssinante().getEmail().equals(emailSolicitante)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Você não tem permissão para acessar esta assinatura");
        }
    }

    private void validarStatusAtivo(Assinatura assinatura) {
        if (assinatura.getStatus() != AssinaturaStatus.ATIVA) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Apenas assinaturas ativas podem ser canceladas");
        }
    }

    // lookups

    private Usuario findUsuarioOrThrow(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private Projeto findProjetoOrThrow(String projetoId) {
        return projetoRepository.findById(projetoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Projeto não encontrado"));
    }

    private Assinatura findAssinaturaOrThrow(String id) {
        return assinaturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Assinatura não encontrada"));
    }


    private AssinaturaResponseDTO toDTO(Assinatura a) {
        AssinaturaResponseDTO dto = new AssinaturaResponseDTO();
        dto.setId(a.getId());
        dto.setAnonima(a.isAnonima());
        dto.setAssinanteId(a.isAnonima() ? null : a.getAssinante().getId());
        dto.setAssinanteNome(a.isAnonima() ? null : a.getAssinante().getNome());
        dto.setProjetoId(a.getProjeto().getId());
        dto.setProjetoTitulo(a.getProjeto().getTitulo());
        dto.setValor(a.getValor());
        dto.setStatus(a.getStatus());
        dto.setRecorrente(a.isRecorrente());
        dto.setInicioEm(a.getInicioEm());
        dto.setCanceladaEm(a.getCanceladaEm());
        dto.setProximaCobrancaEm(a.getProximaCobrancaEm());
        dto.setCriadoEm(a.getCriadoEm());
        return dto;
    }
}