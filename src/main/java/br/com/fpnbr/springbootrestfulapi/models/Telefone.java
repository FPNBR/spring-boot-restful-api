package br.com.fpnbr.springbootrestfulapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "seq_telefone", sequenceName = "seq_telefone", allocationSize = 1, initialValue = 1)
public class Telefone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_telefone")
    private Long id;

    private String numero;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Usuario usuario;
}
