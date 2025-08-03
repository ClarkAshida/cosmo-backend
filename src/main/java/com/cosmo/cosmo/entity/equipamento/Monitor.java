package com.cosmo.cosmo.entity.equipamento;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class Monitor extends Equipamento {

    /**
     * Tamanho da tela em polegadas (ex: 24.0).
     */
    private Double tamanhoTela;

    /**
     * Resolução nativa do monitor (ex: "1920x1080", "2560x1440").
     */
    private String resolucao;
}
