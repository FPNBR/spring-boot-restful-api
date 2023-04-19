package br.com.fpnbr.springbootrestfulapi.controllers;

import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> init() {

        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setLogin("teste1@gmail.com");
        usuario1.setNome("teste1");
        usuario1.setSenha("teste1");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setLogin("teste2@gmail.com");
        usuario2.setNome("teste2");
        usuario2.setSenha("teste2");

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario1);
        usuarios.add(usuario2);

        return new ResponseEntity(usuarios, HttpStatus.OK);
    }
}
