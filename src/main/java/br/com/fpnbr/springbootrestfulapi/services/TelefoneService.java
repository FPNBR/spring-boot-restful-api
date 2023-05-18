package br.com.fpnbr.springbootrestfulapi.services;

import br.com.fpnbr.springbootrestfulapi.dto.TelefoneDTO;
import br.com.fpnbr.springbootrestfulapi.dto.mapper.TelefoneMapper;
import br.com.fpnbr.springbootrestfulapi.models.Telefone;
import br.com.fpnbr.springbootrestfulapi.models.Usuario;
import br.com.fpnbr.springbootrestfulapi.repositories.TelefoneRepository;
import br.com.fpnbr.springbootrestfulapi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelefoneService {
    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private TelefoneMapper telefoneMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    public TelefoneService(TelefoneRepository telefoneRepository, TelefoneMapper telefoneMapper, UsuarioRepository usuarioRepository) {
        this.telefoneRepository = telefoneRepository;
        this.telefoneMapper = telefoneMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public TelefoneDTO criarTelefone(Long usuarioId, TelefoneDTO telefoneDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Telefone telefone = telefoneMapper.toEntity(telefoneDTO);
        telefone.setUsuario(usuario);
        telefone = telefoneRepository.save(telefone);

        return telefoneMapper.toDTO(telefone);
    }

    public TelefoneDTO buscarTelefone(Long usuarioId, Long telefoneId) {
        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado"));

        if (!telefone.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Telefone não pertence ao usuário");
        }

        return telefoneMapper.toDTO(telefone);
    }

    public TelefoneDTO atualizarTelefone(TelefoneDTO telefoneDTO, Long usuarioId) {
        Telefone telefone = telefoneRepository.findById(telefoneDTO.getId())
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado"));

        // Verificar se o telefone pertence ao usuário
        if (!telefone.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Telefone não pertence ao usuário");
        }

        telefone.setNumero(telefoneDTO.getNumero());

        telefone = telefoneRepository.save(telefone);

        return telefoneMapper.toDTO(telefone);
    }

    public void excluirTelefone(Long telefoneId, Long usuarioId) {
        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado"));

        if (!telefone.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Telefone não pertence ao usuário");
        }

        telefoneRepository.deleteById(telefoneId);
    }
}