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

import com.cosmo.cosmo.entity.equipamento.Equipamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long>, JpaSpecificationExecutor<Equipamento> {

    // Métodos para verificar duplicação de campos comuns na criação
    boolean existsByNumeroPatrimonio(String numeroPatrimonio);
    boolean existsBySerialNumber(String serialNumber);

    // Métodos para verificar duplicação de campos comuns na atualização (excluindo o próprio equipamento)
    boolean existsByNumeroPatrimonioAndIdNot(String numeroPatrimonio, Long id);
    boolean existsBySerialNumberAndIdNot(String serialNumber, Long id);

    // Buscar equipamentos por empresa
    List<Equipamento> findByEmpresaId(Long empresaId);

    // Buscar equipamentos por departamento
    List<Equipamento> findByDepartamentoId(Long departamentoId);

    // Buscar equipamentos por tipo específico usando discriminator
    @Query("SELECT e FROM Equipamento e WHERE TYPE(e) = :tipo")
    List<Equipamento> findByTipo(@Param("tipo") Class<? extends Equipamento> tipo);

    // Buscar equipamentos por tipo específico com paginação
    @Query("SELECT e FROM Equipamento e WHERE TYPE(e) = :tipo")
    Page<Equipamento> findByTipo(@Param("tipo") Class<? extends Equipamento> tipo, Pageable pageable);

    // Contar equipamentos por tipo
    @Query("SELECT COUNT(e) FROM Equipamento e WHERE TYPE(e) = :tipo")
    Long countByTipo(@Param("tipo") Class<? extends Equipamento> tipo);
}
