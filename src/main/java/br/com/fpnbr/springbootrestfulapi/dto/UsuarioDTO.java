package br.com.fpnbr.springbootrestfulapi.dto;

import br.com.fpnbr.springbootrestfulapi.models.Telefone;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;

    private String nome;

    private String sobrenome;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    private String cep;

    private String logradouro;

    private String complemento;

    private String bairro;

    private String localidade;

    private String uf;

    private List<Telefone> telefones;
}
