package com.cosmo.cosmo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Equipamento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroPatrimonio;

    @ManyToOne
    private TipoEquipamento tipoEquipamento;

    @ManyToOne
    private Empresa empresa;

    private String siglaEstado;

    private String serialNumber;

    @ManyToOne
    private Departamento departamento;

    private String marca;

    private String modelo;

    private String imei;

    @ManyToOne
    private Usuario usuarioResponsavel;

    private Boolean termoResponsabilidade;

    private String conservacao;
    private String sistemaOperacional;
    private String processador;
    private String armazenamento;
    private String hostname;
    private String dominio;

    private Boolean nAbleOk;
    private Boolean crowdstrikeOk;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}

