package com.example.demo.service;

import com.example.demo.models.Dto.ProjetoRequestDTO;
import com.example.demo.models.Dto.ProjetoResponseDTO;
import com.example.demo.models.Dto.ProjetoUpdateDTO;
import com.example.demo.models.Entity.Categoria;
import com.example.demo.models.Entity.Projeto;
import com.example.demo.models.Entity.Usuario;
import com.example.demo.models.Enums.StatusProjeto;
import com.example.demo.models.Enums.TipoAssinatura;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProjetoRepository;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    // ─── CREATE ───────────────────────────────────────────────────

    @Transactional
    public ProjetoResponseDTO criar(ProjetoRequestDTO dto, MultipartFile imagem, String email) {
        Usuario criador = buscarUsuarioPorEmail(email);
        Categoria categoria = buscarCategoria(dto.getCategoriaId());

        Projeto projeto = new Projeto();
        projeto.setTitulo(dto.getTitulo());
        projeto.setDescricao(dto.getDescricao());
        projeto.setMetaValor(dto.getMetaValor());
        projeto.setDataFim(dto.getDataFim());
        projeto.setTipoAssinatura(dto.getTipoAssinatura());
        projeto.setVideoUrl(dto.getVideoUrl());
        projeto.setCriador(criador);
        projeto.setCategoria(categoria);

        // Lógica segura para salvar a imagem (Arquivo Físico ou Cloudinary)
        if (imagem != null && !imagem.isEmpty()) {
            projeto.setCapaUrl(salvarArquivoLocalmente(imagem));
        } else if (dto.getCapaUrl() != null && !dto.getCapaUrl().trim().isEmpty()) {
            projeto.setCapaUrl(dto.getCapaUrl()); // Salva a URL do Cloudinary enviada pelo front
        } else {
            projeto.setCapaUrl(null); // Pode substituir por um caminho de imagem padrão se desejar
        }

        return ProjetoResponseDTO.from(projetoRepository.save(projeto));
    }

    private String salvarArquivoLocalmente(MultipartFile file) {
        try {
            String diretorio = "uploads/";
            String nomeArquivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path caminhoCompleto = Paths.get(diretorio + nomeArquivo);
            Files.createDirectories(caminhoCompleto.getParent());
            Files.copy(file.getInputStream(), caminhoCompleto);
            return "/uploads/" + nomeArquivo;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar arquivo localmente", e);
        }
    }

    // ─── READ ─────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<ProjetoResponseDTO> listar(StatusProjeto status, String categoriaId, TipoAssinatura tipoAssinatura, String titulo, Pageable pageable) {
        if (titulo != null && !titulo.isBlank() && status != null) {
            return projetoRepository.findByStatusAndTituloContainingIgnoreCase(status, titulo, pageable).map(ProjetoResponseDTO::from);
        }
        if (titulo != null && !titulo.isBlank()) {
            return projetoRepository.findByTituloContainingIgnoreCase(titulo, pageable).map(ProjetoResponseDTO::from);
        }
        if (status != null && categoriaId != null) {
            return projetoRepository.findByStatusAndCategoriaId(status, categoriaId, pageable).map(ProjetoResponseDTO::from);
        }
        if (status != null && tipoAssinatura != null) {
            return projetoRepository.findByStatusAndTipoAssinatura(status, tipoAssinatura, pageable).map(ProjetoResponseDTO::from);
        }
        if (status != null) {
            return projetoRepository.findByStatus(status, pageable).map(ProjetoResponseDTO::from);
        }
        if (categoriaId != null) {
            return projetoRepository.findByCategoriaId(categoriaId, pageable).map(ProjetoResponseDTO::from);
        }
        return projetoRepository.findAll(pageable).map(ProjetoResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public ProjetoResponseDTO buscarPorId(String id) {
        return ProjetoResponseDTO.from(buscarProjeto(id));
    }

    @Transactional(readOnly = true)
    public ProjetoResponseDTO buscarPorSlug(String slug) {
        return ProjetoResponseDTO.from(projetoRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado")));
    }

    @Transactional(readOnly = true)
    public Page<ProjetoResponseDTO> listarPorCriador(String criadorId, StatusProjeto status, Pageable pageable) {
        if (status != null) {
            return projetoRepository.findByCriadorIdAndStatus(criadorId, status, pageable).map(ProjetoResponseDTO::from);
        }
        return projetoRepository.findByCriadorId(criadorId, pageable).map(ProjetoResponseDTO::from);
    }

    // ─── UPDATE ───────────────────────────────────────────────────

    @Transactional
    public ProjetoResponseDTO atualizar(String id, ProjetoUpdateDTO dto, String criadorEmail, boolean isAdmin) {
        Projeto projeto = buscarProjeto(id);
        Usuario solicitante = buscarUsuarioPorEmail(criadorEmail);
        validarPropriedade(projeto, solicitante.getId(), isAdmin);
        Categoria categoria = buscarCategoria(dto.getCategoriaId());

        projeto.setTitulo(dto.getTitulo());
        projeto.setDescricao(dto.getDescricao());
        projeto.setMetaValor(dto.getMetaValor());
        projeto.setDataFim(dto.getDataFim());
        projeto.setTipoAssinatura(dto.getTipoAssinatura());
        projeto.setVideoUrl(dto.getVideoUrl());
        projeto.setCapaUrl(dto.getCapaUrl());
        projeto.setCategoria(categoria);

        if (dto.getStatus() != null && isAdmin) {
            projeto.setStatus(dto.getStatus());
        }

        return ProjetoResponseDTO.from(projetoRepository.save(projeto));
    }

    // ─── STATUS ───────────────────────────────────────────────────

    @Transactional
    public ProjetoResponseDTO atualizarStatus(String id, StatusProjeto novoStatus, String criadorEmail, boolean isAdmin) {
        Projeto projeto = buscarProjeto(id);
        Usuario solicitante = buscarUsuarioPorEmail(criadorEmail);
        validarPropriedade(projeto, solicitante.getId(), isAdmin);
        projeto.setStatus(novoStatus);
        return ProjetoResponseDTO.from(projetoRepository.save(projeto));
    }

    // ─── DELETE ───────────────────────────────────────────────────

    @Transactional
    public void deletar(String id, String criadorEmail, boolean isAdmin) {
        Projeto projeto = buscarProjeto(id);
        Usuario solicitante = buscarUsuarioPorEmail(criadorEmail);
        validarPropriedade(projeto, solicitante.getId(), isAdmin);
        projeto.softDelete();
        projetoRepository.save(projeto);
    }

    // ─── ENCERRAMENTO AUTOMÁTICO ──────────────────────────────────

    @Transactional
    public void encerrarProjetosExpirados() {
        projetoRepository.findByStatusAndDataFimBefore(StatusProjeto.PUBLICADO, LocalDate.now(), Pageable.unpaged())
                .forEach(p -> {
                    p.setStatus(StatusProjeto.ENCERRADO);
                    projetoRepository.save(p);
                });
    }

    // ─── Helpers privados ─────────────────────────────────────────
    private Projeto buscarProjeto(String id) {
        return projetoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado"));
    }

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private Categoria buscarCategoria(String id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    private void validarPropriedade(Projeto projeto, String solicitanteId, boolean isAdmin) {
        if (!isAdmin && !projeto.getCriador().getId().equals(solicitanteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para modificar este projeto");
        }
    }
}