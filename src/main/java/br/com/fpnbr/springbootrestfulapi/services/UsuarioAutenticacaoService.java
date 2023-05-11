package br.com.fpnbr.springbootrestfulapi.services;

import br.com.fpnbr.springbootrestfulapi.config.security.JwtService;
import br.com.fpnbr.springbootrestfulapi.dto.JwtTokenDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioLoginDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioRegistroDTO;
import br.com.fpnbr.springbootrestfulapi.enums.Role;
import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import br.com.fpnbr.springbootrestfulapi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAutenticacaoService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtTokenDTO registro(UsuarioRegistroDTO usuarioRegistroDTO) {
        var usuario = Usuario.builder()
                .nome(usuarioRegistroDTO.getNome())
                .sobrenome(usuarioRegistroDTO.getSobrenome())
                .email(usuarioRegistroDTO.getEmail())
                .senha(passwordEncoder.encode(usuarioRegistroDTO.getSenha()))
                .role(Role.USER)
                .build();

        usuarioRepository.save(usuario);
        var jwtToken = jwtService.generateToken(usuario);

        return JwtTokenDTO.builder()
                .token(jwtToken)
                .build();
    }

    public JwtTokenDTO autenticar(UsuarioLoginDTO usuarioLoginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuarioLoginDTO.getEmail(), usuarioLoginDTO.getSenha()));
        var usuario = usuarioRepository.findUsuarioByEmail(usuarioLoginDTO.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(usuario);

        return JwtTokenDTO.builder()
                .token(jwtToken)
                .build();
    }
}
