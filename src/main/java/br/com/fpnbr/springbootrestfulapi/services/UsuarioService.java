package br.com.fpnbr.springbootrestfulapi.services;

import br.com.fpnbr.springbootrestfulapi.config.security.JwtService;
import br.com.fpnbr.springbootrestfulapi.dto.JwtTokenDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioLoginDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioRegistroDTO;
import br.com.fpnbr.springbootrestfulapi.dto.mapper.UsuarioMapper;
import br.com.fpnbr.springbootrestfulapi.enums.Role;
import br.com.fpnbr.springbootrestfulapi.models.Telefone;
import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import br.com.fpnbr.springbootrestfulapi.repositories.TelefoneRepository;
import br.com.fpnbr.springbootrestfulapi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
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
    private UsuarioMapper usuarioMapper;

    public UsuarioRegistroDTO criarUsuario(UsuarioRegistroDTO usuarioRegistroDTO) {
        Usuario usuario = Usuario.builder()
                .id(usuarioRegistroDTO.getId())
                .nome(usuarioRegistroDTO.getNome())
                .sobrenome(usuarioRegistroDTO.getSobrenome())
                .email(usuarioRegistroDTO.getEmail())
                .senha(passwordEncoder.encode(usuarioRegistroDTO.getSenha()))
                .telefones(usuarioRegistroDTO.getTelefones())
                .role(Role.USER)
                .build();

        if (usuarioRegistroDTO.getTelefones() != null) {
            for (Telefone telefone : usuarioRegistroDTO.getTelefones()) {
                telefone.setUsuario(usuario);
            }
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.ToDto(usuarioSalvo);
    }

    public JwtTokenDTO autenticarUsuario(UsuarioLoginDTO usuarioLoginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuarioLoginDTO.getEmail(), usuarioLoginDTO.getSenha()));
        var usuario = usuarioRepository.findUsuarioByEmail(usuarioLoginDTO.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(usuario);

        return JwtTokenDTO.builder()
                .token(jwtToken)
                .build();
    }

    public UsuarioRegistroDTO buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            return usuarioMapper.ToDto(usuario);
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    public UsuarioRegistroDTO atualizarUsuario(Long id, UsuarioRegistroDTO usuarioRegistroDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            Usuario usuarioAtualizado = Usuario.builder()
                    .id(usuario.getId())
                    .nome(usuarioRegistroDTO.getNome())
                    .sobrenome(usuarioRegistroDTO.getSobrenome())
                    .email(usuarioRegistroDTO.getEmail())
                    .senha(usuarioRegistroDTO.getSenha() != null ? passwordEncoder.encode(usuarioRegistroDTO.getSenha()) : usuario.getSenha())
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
