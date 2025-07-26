package com.cosmo.cosmo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataEntrega;
    @Column(nullable = true)
    private LocalDateTime dataDevolucao;

    @Column(columnDefinition = "TEXT")
    private String observacoesEntrega;
    @Column(columnDefinition = "TEXT")
    private String observacoesDevolucao;

    @Column(nullable = true)
    private String urlTermoEntrega;
    @Column(nullable = true)
    private String urlTermoDevolucao;

}
