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

import com.cosmo.cosmo.entity.equipamento.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {

    // Buscar monitores por tamanho da tela
    List<Monitor> findByTamanhoTela(Double tamanhoTela);

    // Buscar monitores por resolução
    List<Monitor> findByResolucao(String resolucao);

    // Buscar monitores por faixa de tamanho
    @Query("SELECT m FROM Monitor m WHERE m.tamanhoTela BETWEEN :tamanhoMin AND :tamanhoMax")
    List<Monitor> findByTamanhoTelaRange(@Param("tamanhoMin") Double tamanhoMin, @Param("tamanhoMax") Double tamanhoMax);

    // Buscar monitores grandes (acima de 24 polegadas)
    @Query("SELECT m FROM Monitor m WHERE m.tamanhoTela > 24.0")
    List<Monitor> findMonitoresGrandes();

    // Buscar monitores com resolução 4K
    @Query("SELECT m FROM Monitor m WHERE m.resolucao LIKE '%3840x2160%' OR m.resolucao LIKE '%4K%'")
    List<Monitor> findMonitores4K();
}
