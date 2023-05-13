package br.com.fpnbr.springbootrestfulapi.repositories;

import br.com.fpnbr.springbootrestfulapi.models.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefone, Integer> {
}
