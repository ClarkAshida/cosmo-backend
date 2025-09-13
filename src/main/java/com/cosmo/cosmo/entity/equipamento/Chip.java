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
