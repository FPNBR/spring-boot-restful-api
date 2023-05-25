package br.com.fpnbr.springbootrestfulapi.controllers;

import br.com.fpnbr.springbootrestfulapi.dto.JwtTokenDTO;
import br.com.fpnbr.springbootrestfulapi.dto.TelefoneDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioLoginDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioDTO;
import br.com.fpnbr.springbootrestfulapi.repositories.TelefoneRepository;
import br.com.fpnbr.springbootrestfulapi.repositories.UsuarioRepository;
import br.com.fpnbr.springbootrestfulapi.services.TelefoneService;
import br.com.fpnbr.springbootrestfulapi.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TelefoneService telefoneService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/")
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioCriado = usuarioService.criarUsuario(usuarioDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> autenticarUsuario(@RequestBody UsuarioLoginDTO usuarioLoginDTO) {

        return ResponseEntity.ok(usuarioService.autenticarUsuario(usuarioLoginDTO));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        usuarioService.refreshToken(request, response);
    }

    @GetMapping("/")
    public List<UsuarioDTO> buscarTodosUsuarios() {
        return usuarioService.buscarTodosUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.buscarUsuarioPorId(id);

        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);

        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{usuarioId}/telefone")
    public ResponseEntity<TelefoneDTO> criarTelefone(@PathVariable Long usuarioId, @RequestBody TelefoneDTO telefoneDTO) {
        telefoneDTO.setUsuarioId(usuarioId);
        TelefoneDTO telefoneCriado = telefoneService.criarTelefone(usuarioId, telefoneDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(telefoneCriado);
    }

    @GetMapping("/{usuarioId}/telefone/{telefoneId}")
    public ResponseEntity<TelefoneDTO> buscarTelefone(@PathVariable Long usuarioId, @PathVariable Long telefoneId) {
        TelefoneDTO telefoneDTO = telefoneService.buscarTelefone(usuarioId, telefoneId);

        return ResponseEntity.ok(telefoneDTO);
    }

    @PutMapping("/{usuarioId}/telefone/{telefoneId}")
    public ResponseEntity<TelefoneDTO> atualizarTelefone(@PathVariable Long usuarioId, @PathVariable Long telefoneId, @RequestBody TelefoneDTO telefoneDTO) {
        telefoneDTO.setId(telefoneId);
        TelefoneDTO telefoneAtualizado = telefoneService.atualizarTelefone(telefoneDTO, usuarioId);

        return ResponseEntity.ok(telefoneAtualizado);
    }

    @DeleteMapping("/{usuarioId}/telefone/{telefoneId}")
    public ResponseEntity<Void> excluirTelefone(@PathVariable Long usuarioId, @PathVariable Long telefoneId) {
        telefoneService.excluirTelefone(telefoneId, usuarioId);

        return ResponseEntity.ok().build();
    }
}
