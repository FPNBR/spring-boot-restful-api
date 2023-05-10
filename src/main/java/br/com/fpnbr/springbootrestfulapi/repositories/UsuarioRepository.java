package br.com.fpnbr.springbootrestfulapi.repositories;

import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("select u from Usuario u where u.login = ?1")
    Usuario findUsuarioByLogin(String login);
}
