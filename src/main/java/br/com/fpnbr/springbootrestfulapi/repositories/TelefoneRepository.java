package br.com.fpnbr.springbootrestfulapi.repositories;

import br.com.fpnbr.springbootrestfulapi.models.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
    List<Telefone> findByUsuarioId(Long usuarioId);}
