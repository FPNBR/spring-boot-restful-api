package br.com.fpnbr.springbootrestfulapi.dto.mapper;

import br.com.fpnbr.springbootrestfulapi.dto.UsuarioRegistroDTO;
import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    private final ModelMapper modelMapper;

    public UsuarioMapper() {
        this.modelMapper = new ModelMapper();
    }

    public Usuario ToEntity(UsuarioRegistroDTO dto) {
        return Usuario.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .sobrenome(dto.getSobrenome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .telefones(dto.getTelefones())
                .build();
    }

    public UsuarioRegistroDTO ToDto(Usuario entity) {
        return UsuarioRegistroDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .sobrenome(entity.getSobrenome())
                .email(entity.getEmail())
                .senha(entity.getSenha())
                .telefones(entity.getTelefones())
                .build();
    }
}
