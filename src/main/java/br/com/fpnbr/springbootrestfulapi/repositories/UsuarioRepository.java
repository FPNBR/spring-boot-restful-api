package br.com.fpnbr.springbootrestfulapi.repositories;

import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
