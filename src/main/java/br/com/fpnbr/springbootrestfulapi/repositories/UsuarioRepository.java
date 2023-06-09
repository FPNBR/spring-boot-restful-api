package br.com.fpnbr.springbootrestfulapi.repositories;

import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findUsuarioByEmail(String email);
}
