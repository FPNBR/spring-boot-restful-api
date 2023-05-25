package br.com.fpnbr.springbootrestfulapi.services;

import br.com.fpnbr.springbootrestfulapi.config.security.JwtService;
import br.com.fpnbr.springbootrestfulapi.dto.JwtTokenDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioLoginDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioDTO;
import br.com.fpnbr.springbootrestfulapi.dto.mapper.UsuarioMapper;
import br.com.fpnbr.springbootrestfulapi.enums.Role;
import br.com.fpnbr.springbootrestfulapi.enums.TokenType;
import br.com.fpnbr.springbootrestfulapi.models.Telefone;
import br.com.fpnbr.springbootrestfulapi.models.Token;
import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import br.com.fpnbr.springbootrestfulapi.repositories.TelefoneRepository;
import br.com.fpnbr.springbootrestfulapi.repositories.TokenRepository;
import br.com.fpnbr.springbootrestfulapi.repositories.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private static final String API_URL = "https://viacep.com.br/ws/";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    public JwtTokenDTO autenticarUsuario(UsuarioLoginDTO usuarioLoginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuarioLoginDTO.getEmail(), usuarioLoginDTO.getSenha()));
        var usuario = usuarioRepository.findUsuarioByEmail(usuarioLoginDTO.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);

        return JwtTokenDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(Usuario usuario, String jwtToken) {
        var token = Token.builder()
                .usuario(usuario)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Usuario usuario) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(usuario.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.usuarioRepository.findUsuarioByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = JwtTokenDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        RestTemplate restTemplate = new RestTemplate();

        String cep = usuarioDTO.getCep();
        String url = API_URL + cep + "/json/";

        ResponseEntity<UsuarioDTO> response = restTemplate.getForEntity(url, UsuarioDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            UsuarioDTO cepResponse = response.getBody();
            if (cepResponse != null) {
                usuarioDTO.setLogradouro(cepResponse.getLogradouro());
                usuarioDTO.setComplemento(cepResponse.getComplemento());
                usuarioDTO.setBairro(cepResponse.getBairro());
                usuarioDTO.setLocalidade(cepResponse.getLocalidade());
                usuarioDTO.setUf(cepResponse.getUf());
            }
        }

        Usuario usuario = Usuario.builder()
                .id(usuarioDTO.getId())
                .nome(usuarioDTO.getNome())
                .sobrenome(usuarioDTO.getSobrenome())
                .email(usuarioDTO.getEmail())
                .senha(passwordEncoder.encode(usuarioDTO.getSenha()))
                .cep(usuarioDTO.getCep())
                .logradouro(usuarioDTO.getLogradouro())
                .complemento(usuarioDTO.getComplemento())
                .bairro(usuarioDTO.getBairro())
                .localidade(usuarioDTO.getLocalidade())
                .uf(usuarioDTO.getUf())
                .telefones(usuarioDTO.getTelefones())
                .role(Role.USER)
                .build();

        if (usuarioDTO.getTelefones() != null) {
            for (Telefone telefone : usuarioDTO.getTelefones()) {
                telefone.setUsuario(usuario);
            }
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.ToDto(usuarioSalvo);
    }

    public List<UsuarioDTO> buscarTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarioMapper.mapList(usuarios, UsuarioDTO.class);
    }

    public UsuarioDTO buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            return usuarioMapper.ToDto(usuario);
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            Usuario usuarioAtualizado = Usuario.builder()
                    .id(usuario.getId())
                    .nome(usuarioDTO.getNome())
                    .sobrenome(usuarioDTO.getSobrenome())
                    .email(usuarioDTO.getEmail())
                    .senha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : usuario.getSenha())
                    .telefones(usuario.getTelefones())
                    .build();

            usuarioRepository.save(usuarioAtualizado);

            return usuarioMapper.ToDto(usuarioAtualizado);
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    public void deletarUsuario(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuarioRepository.delete(usuario);

        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }
}
