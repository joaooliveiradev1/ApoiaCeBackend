package com.example.demo.service;

import com.example.demo.models.Dto.CategoriaRequestDTO;
import com.example.demo.models.Dto.CategoriaResponseDTO;
import com.example.demo.models.Entity.Categoria;
import com.example.demo.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(CategoriaResponseDTO::fromEntity)
                .toList();
    }

    public List<CategoriaResponseDTO> buscarPorNome(String nome) {
        return categoriaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(CategoriaResponseDTO::fromEntity)
                .toList();
    }

    public CategoriaResponseDTO buscarPorId(String id) {
        return categoriaRepository.findByIdAndDeletedAtIsNull(id)
                .map(CategoriaResponseDTO::fromEntity)
                .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));
    }

    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO request) {
        if (categoriaRepository.existsByNomeIgnoreCaseAndDeletedAtIsNull(request.getNome())) {
            throw new IllegalArgumentException("Já existe uma categoria com esse nome");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(request.getNome());
        categoria.setCor(request.getCor());

        return CategoriaResponseDTO.fromEntity(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponseDTO atualizar(String id, CategoriaRequestDTO request) {
        Categoria categoria = categoriaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));

        if (categoriaRepository.existsByNomeIgnoreCaseAndDeletedAtIsNullAndIdNot(request.getNome(), id)) {
            throw new IllegalArgumentException("Já existe uma categoria com esse nome");
        }

        categoria.setNome(request.getNome());
        categoria.setCor(request.getCor());

        return CategoriaResponseDTO.fromEntity(categoriaRepository.save(categoria));
    }

    @Transactional
    public void deletar(String id) {
        Categoria categoria = categoriaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));

        categoria.setDeletedAt(OffsetDateTime.now());
        categoriaRepository.save(categoria);
    }
}