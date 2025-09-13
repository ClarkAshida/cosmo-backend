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
package com.cosmo.cosmo.dto.historico;

import com.cosmo.cosmo.enums.StatusEquipamento;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucaoEquipamentoDTO {

    @Size(max = 1000, message = "Observações de devolução devem ter no máximo 1000 caracteres")
    private String observacoesDevolucao;

    @Size(max = 255, message = "URL do termo de devolução deve ter no máximo 255 caracteres")
    private String urlTermoDevolucao;

    private StatusEquipamento novoStatus; // Se não informado, será DISPONIVEL por padrão
}
