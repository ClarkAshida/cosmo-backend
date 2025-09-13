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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OperacaoMultiplaResponseDTO extends RepresentationModel<OperacaoMultiplaResponseDTO> {

    private Integer totalItens;
    private Integer itensSucesso;
    private Integer itensErro;
    private List<HistoricoResponseDTO> historicosProcessados; // Históricos criados/atualizados com sucesso
    private List<String> erros; // Lista de erros encontrados durante o processamento
    private String observacaoGeral; // Observação sobre a operação como um todo
}
