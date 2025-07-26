package com.cosmo.cosmo.entity;

import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.enums.TipoEquipamento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Equipamento {

    // Identificador único do equipamento
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroPatrimonio;
    private String serialNumber;
    private String imei;
    
    // Informações de identificação do equipamento
    private TipoEquipamento tipoEquipamento;
    private String marca;
    private String modelo;
    private EstadoConservacao estadoConservacao;

    private Boolean termoResponsabilidade;

    // Localização do equipamento
    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
    private String siglaEstado;
    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    // Informações técnicas do equipamento
    private String sistemaOperacional;
    private String processador;
    private String armazenamento;
    private String hostname;
    private String dominio;

    // Configuração de segurança
    private Boolean remoteAccessEnabled;
    private Boolean antivirusEnabled;

    // Informações de compra
    private Float valor;
    private String notaFiscal;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEquipamento status;
}

