package br.com.fpnbr.springbootrestfulapi.controllers;

import br.com.fpnbr.springbootrestfulapi.dto.JwtTokenDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioLoginDTO;
import br.com.fpnbr.springbootrestfulapi.dto.UsuarioRegistroDTO;
import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import br.com.fpnbr.springbootrestfulapi.repositories.UsuarioRepository;
import br.com.fpnbr.springbootrestfulapi.services.UsuarioAutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/")
    public ResponseEntity<JwtTokenDTO> registrar(@RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        return ResponseEntity.ok(usuarioAutenticacaoService.registro(usuarioRegistroDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> autenticar(@RequestBody UsuarioLoginDTO usuarioLoginDTO) {
        return ResponseEntity.ok(usuarioAutenticacaoService.autenticar(usuarioLoginDTO));
    }

    @GetMapping("/")
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable (value = "id") Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> deletar(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isPresent()) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
