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
package com.cosmo.cosmo.repository.equipamento;

import com.cosmo.cosmo.entity.equipamento.Chip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChipRepository extends JpaRepository<Chip, Long> {

    // Métodos para verificar duplicação de campos específicos de Chip na criação
    boolean existsByIccid(String iccid);
    boolean existsByNumeroTelefone(String numeroTelefone);

    // Métodos para verificar duplicação na atualização (excluindo o próprio equipamento)
    boolean existsByIccidAndIdNot(String iccid, Long id);
    boolean existsByNumeroTelefoneAndIdNot(String numeroTelefone, Long id);
}
