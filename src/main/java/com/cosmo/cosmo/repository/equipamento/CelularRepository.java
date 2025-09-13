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

import com.cosmo.cosmo.entity.equipamento.Celular;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelularRepository extends JpaRepository<Celular, Long> {

    // Métodos para verificar duplicação de campos específicos de Celular na criação
    boolean existsByImei(String imei);
    boolean existsByImei2(String imei2);
    boolean existsByEid(String eid);

    // Métodos para verificar duplicação na atualização (excluindo o próprio equipamento)
    boolean existsByImeiAndIdNot(String imei, Long id);
    boolean existsByImei2AndIdNot(String imei2, Long id);
    boolean existsByEidAndIdNot(String eid, Long id);
}
