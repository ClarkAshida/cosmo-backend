package com.cosmo.cosmo.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String cargo;
    private String cpf;
    private String cidade;
    @ManyToOne
    private Departamento departamento;
    private Boolean ativo;

}
