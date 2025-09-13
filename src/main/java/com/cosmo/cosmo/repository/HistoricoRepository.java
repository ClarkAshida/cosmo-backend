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
package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.Historico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long>, JpaSpecificationExecutor<Historico> {

    // Encontrar históricos por usuário
    List<Historico> findByUsuarioId(Long usuarioId);

    // Encontrar históricos por usuário com paginação
    Page<Historico> findByUsuarioId(Long usuarioId, Pageable pageable);

    // Encontrar históricos por equipamento
    List<Historico> findByEquipamentoId(Long equipamentoId);

    // Encontrar históricos por equipamento com paginação
    Page<Historico> findByEquipamentoId(Long equipamentoId, Pageable pageable);
}
