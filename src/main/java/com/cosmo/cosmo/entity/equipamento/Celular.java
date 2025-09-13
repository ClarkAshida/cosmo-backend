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
