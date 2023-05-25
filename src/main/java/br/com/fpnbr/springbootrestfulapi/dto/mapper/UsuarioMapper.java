package br.com.fpnbr.springbootrestfulapi.dto.mapper;

import br.com.fpnbr.springbootrestfulapi.dto.UsuarioDTO;
import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    private final ModelMapper modelMapper;

    public UsuarioMapper() {
        this.modelMapper = new ModelMapper();
    }

    public Usuario ToEntity(UsuarioDTO dto) {
        return Usuario.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .sobrenome(dto.getSobrenome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .cep(dto.getCep())
                .logradouro(dto.getLogradouro())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .localidade(dto.getLocalidade())
                .uf(dto.getUf())
                .telefones(dto.getTelefones())
                .build();
    }

    public UsuarioDTO ToDto(Usuario entity) {
        return UsuarioDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .sobrenome(entity.getSobrenome())
                .email(entity.getEmail())
                .senha(entity.getSenha())
                .cep(entity.getCep())
                .logradouro(entity.getLogradouro())
                .complemento(entity.getComplemento())
                .bairro(entity.getBairro())
                .localidade(entity.getLocalidade())
                .uf(entity.getUf())
                .telefones(entity.getTelefones())
                .build();
    }

    public <S, T> List<T> mapList(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream()
                .map(source -> modelMapper.map(source, targetClass))
                .collect(Collectors.toList());
    }
}
