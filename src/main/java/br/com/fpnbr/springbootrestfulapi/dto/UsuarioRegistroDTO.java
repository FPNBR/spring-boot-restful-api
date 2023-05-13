package br.com.fpnbr.springbootrestfulapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistroDTO {
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private List<TelefoneDTO> telefones;
}
