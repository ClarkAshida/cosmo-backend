package com.cosmo.cosmo.entity.equipamento;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Chip extends Equipamento {

    /**
     * Número da linha telefônica associada ao chip.
     */
    private String numeroTelefone;

    /**
     * Código ICCID do chip, identificador único do SIM card.
     */
    @Column(length = 22, unique = true)
    private String iccid;

    /**
     * Operadora do chip (ex: "Vivo", "Claro", "TIM").
     */
    private String operadora;

    /**
     * Descrição do plano de dados/voz contratado.
     */
    private String tipoPlano;
}
