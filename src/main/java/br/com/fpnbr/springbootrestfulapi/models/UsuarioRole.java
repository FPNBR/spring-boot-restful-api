package br.com.fpnbr.springbootrestfulapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "usuario_id"})})
@SequenceGenerator(name = "seq_usuario_role", sequenceName = "seq_usuario_role", allocationSize = 1, initialValue = 1)
public class UsuarioRole {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario_role")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
