package br.com.fpnbr.springbootrestfulapi.controllers;

import br.com.fpnbr.springbootrestfulapi.dto.JwtTokenDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioLoginDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioRegistroDTO;
import br.com.fpnbr.springbootrestfulapi.models.Telefone;
import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import br.com.fpnbr.springbootrestfulapi.repositories.TelefoneRepository;
import br.com.fpnbr.springbootrestfulapi.repositories.UsuarioRepository;
import br.com.fpnbr.springbootrestfulapi.services.UsuarioAutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioAutenticacaoService usuarioAutenticacaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/")
    public ResponseEntity<JwtTokenDTO> criarUsuario(@RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        return ResponseEntity.ok(usuarioAutenticacaoService.registro(usuarioRegistroDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> autenticarUsuario(@RequestBody UsuarioLoginDTO usuarioLoginDTO) {
        return ResponseEntity.ok(usuarioAutenticacaoService.autenticar(usuarioLoginDTO));
    }

    @GetMapping("/")
    public ResponseEntity<List<Usuario>> buscarTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable (value = "id") Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();

            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setSobrenome(usuarioAtualizado.getSobrenome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));

            Usuario usuarioSalvo = usuarioRepository.save(usuario);

            return new ResponseEntity<>(usuarioSalvo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> deletarUsuario(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isPresent()) {
            usuarioRepository.deleteById(id);

            return ResponseEntity.ok().build();

        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/telefone")
    public ResponseEntity<Telefone> criarTelefone(@PathVariable Integer id, @RequestBody Telefone telefone) {
        System.out.println("Usuario ID:" + id);

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            telefone.setUsuario(usuario);
            Telefone telefoneSalvo = telefoneRepository.save(telefone);
            return new ResponseEntity<>(telefoneSalvo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/telefone/{id}")
    public ResponseEntity<Telefone> atualizarTelefone(@PathVariable Integer id, @RequestBody Telefone telefoneAtualizado) {
        Optional<Telefone> telefoneExistente = telefoneRepository.findById(id);

        if (telefoneExistente.isPresent()) {
            Telefone telefone = telefoneExistente.get();
            telefone.setNumero(telefoneAtualizado.getNumero());
            Telefone telefoneSalvo = telefoneRepository.save(telefone);

            return new ResponseEntity<>(telefoneSalvo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/telefone/{id}")
    public ResponseEntity<Void> excluirTelefone(@PathVariable Integer id) {
        Optional<Telefone> telefoneExistente = telefoneRepository.findById(id);

        if (telefoneExistente.isPresent()) {
            telefoneRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
