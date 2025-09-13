/*
 * Copyright 2025 Flávio Alexandre Orrico Severiano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
