package com.example.demo.service;

import com.example.demo.models.Dto.LoginRequestDTO;
import com.example.demo.models.Dto.LoginResponseDTO;
import com.example.demo.models.Dto.UsuarioCreateRequest;
import com.example.demo.models.Entity.Usuario;
import com.example.demo.models.Enums.UsuarioRole;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        var usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        String token = jwtService.gerarToken(usuario.getEmail());

        LoginResponseDTO resp = new LoginResponseDTO();
        resp.setToken(token);
        resp.setUsuarioId(usuario.getId());
        resp.setNome(usuario.getNome());
        resp.setEmail(usuario.getEmail());
        resp.setRole(usuario.getRole());
        return resp;
    }

    public Usuario registro(UsuarioCreateRequest dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario newUser = new Usuario();
        newUser.setNome(dto.getNome());
        newUser.setEmail(dto.getEmail());
        newUser.setSenhaHash(passwordEncoder.encode(dto.getSenha())); // hash aqui
        newUser.setRole(UsuarioRole.APOIADOR);
        newUser.setCpf(dto.getCpf());
        newUser.setDataNascimento(dto.getDataNascimento());

        return usuarioRepository.save(newUser);
    }
}
