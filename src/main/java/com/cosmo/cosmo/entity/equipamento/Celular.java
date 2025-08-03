package com.cosmo.cosmo.entity.equipamento;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class Celular extends Equipamento {

    /**
     * IMEI principal do dispositivo. Deve ser único.
     */
    @Column(unique = true, length = 15)
    private String imei;

    /**
     * IMEI do segundo SIM, se houver.
     */
    @Column(unique = true, length = 15)
    private String imei2;

    /**
     * Identificador único do eSIM, se houver.
     */
    @Column(length = 32, unique = true)
    private String eid;

    /**
     * Indica se o dispositivo é gerenciado por uma solução de MDM (Mobile Device Management).
     */
    @Column(name = "gerenciado_por_mdm")
    private Boolean gerenciadoPorMDM;

    /**
     * Indica qual é o MDM que está configurado no aparelho.
     */
    @Column(name = "mdm")
    private String MDM;
}
