package com.example.demo.service;
import java.util.Map;
import com.example.demo.models.Dto.*;
import com.example.demo.models.Entity.PasswordResetToken;
import com.example.demo.models.Entity.Usuario;
import com.example.demo.models.Enums.UsuarioRole;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder, PasswordResetTokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        var usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        String token = jwtService.gerarToken(usuario.getEmail(), Map.of(
                "id", usuario.getId(),
                "nome", usuario.getNome(),
                "role", usuario.getRole().name()
        ));

        LoginResponseDTO resp = new LoginResponseDTO();
        resp.setToken(token);
        resp.setUsuarioId(usuario.getId());
        resp.setNome(usuario.getNome());
        resp.setEmail(usuario.getEmail());
        resp.setRole(usuario.getRole());
        return resp;
    }

    public LoginResponseDTO registro(UsuarioCreateRequest dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        UsuarioRole role = dto.getRole();
        if (role == null || role == UsuarioRole.ADMIN) {
            role = UsuarioRole.APOIADOR;
        }

        Usuario newUser = new Usuario();
        newUser.setNome(dto.getNome());
        newUser.setEmail(dto.getEmail());
        newUser.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        newUser.setRole(role);
        newUser.setCpf(dto.getCpf());
        newUser.setDataNascimento(dto.getDataNascimento());

        Usuario saved = usuarioRepository.save(newUser);

        String token = jwtService.gerarToken(saved.getEmail(), Map.of(
                "id", saved.getId(),
                "nome", saved.getNome(),
                "role", saved.getRole().name()
        ));

        LoginResponseDTO resp = new LoginResponseDTO();
        resp.setToken(token);
        resp.setUsuarioId(saved.getId());
        resp.setNome(saved.getNome());
        resp.setEmail(saved.getEmail());
        resp.setRole(saved.getRole());
        return resp;
    }

    @Transactional
    public String forgotPassword(ForgotPasswordRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        tokenRepository.deleteByUsuarioId(usuario.getId());

        String tokenValor = UUID.randomUUID().toString();

        while (tokenRepository.existsByToken(tokenValor)) {
            tokenValor = UUID.randomUUID().toString();
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenValor);
        token.setUsuario(usuario);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        tokenRepository.save(token);

        return tokenValor;
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDTO dto) {
        PasswordResetToken resetToken = tokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Token inválido"));

        if (resetToken.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token já utilizado");
        }

        if (resetToken.isExpired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setSenhaHash(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(usuario);

        resetToken.setUsedAt(LocalDateTime.now());
        tokenRepository.save(resetToken);
    }
}