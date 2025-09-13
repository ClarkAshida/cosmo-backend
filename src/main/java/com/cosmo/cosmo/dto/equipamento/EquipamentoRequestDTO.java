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
package com.cosmo.cosmo.dto.equipamento;

import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.enums.TipoEquipamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoRequestDTO {

    // Identificadores únicos
    @Size(max = 50, message = "Número de patrimônio deve ter no máximo 50 caracteres")
    private String numeroPatrimonio;

    @Size(max = 100, message = "Serial number deve ter no máximo 100 caracteres")
    private String serialNumber;

    @Size(max = 15, message = "IMEI deve ter no máximo 15 caracteres")
    private String imei;

    // Propriedades específicas para equipamentos celular
    @Size(max = 32, message = "EID deve ter no máximo 32 caracteres")
    private String eid;

    @Size(max = 20, message = "Número de telefone deve ter no máximo 20 caracteres")
    private String numeroTelefone;

    @Size(max = 20, message = "ICCID deve ter no máximo 20 caracteres")
    private String iccid;

    // Informações de identificação do equipamento
    @NotNull(message = "Tipo do equipamento é obrigatório")
    private TipoEquipamento tipoEquipamento;

    @Size(max = 50, message = "Marca deve ter no máximo 50 caracteres")
    private String marca;

    @Size(max = 100, message = "Modelo deve ter no máximo 100 caracteres")
    private String modelo;

    private EstadoConservacao estadoConservacao;
    private Boolean termoResponsabilidade;

    // Localização do equipamento
    @NotNull(message = "Empresa é obrigatória")
    private Long empresaId;

    @Size(max = 2, message = "Sigla do estado deve ter no máximo 2 caracteres")
    private String siglaEstado;

    @NotNull(message = "Departamento é obrigatório")
    private Long departamentoId;

    // Informações técnicas do equipamento
    @Size(max = 100, message = "Sistema operacional deve ter no máximo 100 caracteres")
    private String sistemaOperacional;

    @Size(max = 100, message = "Processador deve ter no máximo 100 caracteres")
    private String processador;

    @Size(max = 50, message = "Armazenamento deve ter no máximo 50 caracteres")
    private String armazenamento;

    @Size(max = 100, message = "Hostname deve ter no máximo 100 caracteres")
    private String hostname;

    @Size(max = 100, message = "Domínio deve ter no máximo 100 caracteres")
    private String dominio;

    // Configuração de segurança
    private Boolean remoteAccessEnabled;
    private Boolean antivirusEnabled;

    // Informações de compra
    private Float valor;

    @Size(max = 50, message = "Nota fiscal deve ter no máximo 50 caracteres")
    private String notaFiscal;

    @Size(max = 1000, message = "Observações devem ter no máximo 1000 caracteres")
    private String observacoes;

    @NotNull(message = "Status do equipamento é obrigatório")
    private StatusEquipamento status;
}
