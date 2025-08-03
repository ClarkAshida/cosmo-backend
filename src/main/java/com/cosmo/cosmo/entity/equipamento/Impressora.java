package com.cosmo.cosmo.entity.equipamento;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class Impressora extends Equipamento {

    /**
     * Tipo da impressora (ex: "Laser", "Jato de Tinta", "Térmica").
     */
    @Column(name = "tipo_impressora")
    private String tipoImpressora;

    /**
     * Indica se a impressora é colorida.
     */
    private Boolean colorida;

    /**
     * Indica se possui funções de scanner, cópia, etc.
     */
    private Boolean multifuncional;

    /**
     * Endereço IP da impressora na rede.
     */
    @Column(name = "endereco_ip")
    private String enderecoIP;

    /**
     * Código ou modelo do suprimento (toner, cartucho) utilizado.
     */
    @Column(name = "modelo_suprimento")
    private String modeloSuprimento;
}
