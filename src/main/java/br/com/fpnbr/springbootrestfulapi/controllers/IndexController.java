package br.com.fpnbr.springbootrestfulapi.controllers;

import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import br.com.fpnbr.springbootrestfulapi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "site1.com")
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<Usuario>> usuario() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> buscar(@PathVariable (value = "id") Long id) {
       Optional<Usuario> usuario = usuarioRepository.findById(id);

        return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        for (int i = 0; i < usuario.getTelefones().size(); i++) {
            usuario.getTelefones().get(i).setUsuario(usuario);
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @PutMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {
        for (int i = 0; i < usuario.getTelefones().size(); i++) {
            usuario.getTelefones().get(i).setUsuario(usuario);
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> deletar(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isPresent()) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
