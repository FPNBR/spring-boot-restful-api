package br.com.fpnbr.springbootrestfulapi.dto.mapper;

import br.com.fpnbr.springbootrestfulapi.dto.TelefoneDTO;
import br.com.fpnbr.springbootrestfulapi.models.Telefone;
import org.springframework.stereotype.Component;

@Component
public class TelefoneMapper {

    public TelefoneDTO toDTO(Telefone telefone) {
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .usuarioId(telefone.getUsuario().getId())
                .build();
    }

    public Telefone toEntity(TelefoneDTO dto) {
        return Telefone.builder()
                .id(dto.getId())
                .numero(dto.getNumero())
                .build();
    }
}

