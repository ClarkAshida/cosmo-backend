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

import com.cosmo.cosmo.entity.equipamento.Desktop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesktopRepository extends JpaRepository<Desktop, Long> {

    // Buscar desktops por hostname
    List<Desktop> findByHostname(String hostname);

    // Buscar desktops por sistema operacional
    List<Desktop> findBySistemaOperacional(String sistemaOperacional);

    // Verificar se hostname já existe (excluindo o próprio equipamento)
    boolean existsByHostnameAndIdNot(String hostname, Long id);

    // Buscar desktops com acesso remoto habilitado
    @Query("SELECT d FROM Desktop d WHERE d.remoteAccessEnabled = true")
    List<Desktop> findWithRemoteAccessEnabled();

    // Buscar desktops sem antivírus
    @Query("SELECT d FROM Desktop d WHERE d.antivirusEnabled = false OR d.antivirusEnabled IS NULL")
    List<Desktop> findWithoutAntivirus();
}
