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
package com.cosmo.cosmo.specification;

import com.cosmo.cosmo.entity.equipamento.Equipamento;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoSpecification {

    public static Specification<Equipamento> comFiltros(
            String serialNumber,
            String numeroPatrimonio,
            String marca,
            String modelo,
            String estadoConservacao,
            String status,
            Boolean termoResponsabilidade,
            String notaFiscal,
            String statusPropriedade) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por serial number (busca parcial e insensível a maiúsculas/minúsculas)
            if (serialNumber != null && !serialNumber.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("serialNumber")),
                    "%" + serialNumber.toLowerCase() + "%"
                ));
            }

            // Filtro por número do patrimônio (busca parcial e insensível a maiúsculas/minúsculas)
            if (numeroPatrimonio != null && !numeroPatrimonio.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("numeroPatrimonio")),
                    "%" + numeroPatrimonio.toLowerCase() + "%"
                ));
            }

            // Filtro por marca (busca parcial e insensível a maiúsculas/minúsculas)
            if (marca != null && !marca.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("marca")),
                    "%" + marca.toLowerCase() + "%"
                ));
            }

            // Filtro por modelo (busca parcial e insensível a maiúsculas/minúsculas)
            if (modelo != null && !modelo.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("modelo")),
                    "%" + modelo.toLowerCase() + "%"
                ));
            }

            // Filtro por estado de conservação (busca parcial e insensível a maiúsculas/minúsculas)
            if (estadoConservacao != null && !estadoConservacao.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("estadoConservacao").as(String.class)),
                    "%" + estadoConservacao.toLowerCase() + "%"
                ));
            }

            // Filtro por status (busca parcial e insensível a maiúsculas/minúsculas)
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("status").as(String.class)),
                    "%" + status.toLowerCase() + "%"
                ));
            }

            // Filtro por termo de responsabilidade (busca exata para booleano)
            if (termoResponsabilidade != null) {
                predicates.add(criteriaBuilder.equal(root.get("termoResponsabilidade"), termoResponsabilidade));
            }

            // Filtro por nota fiscal (busca parcial e insensível a maiúsculas/minúsculas)
            if (notaFiscal != null && !notaFiscal.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("notaFiscal")),
                    "%" + notaFiscal.toLowerCase() + "%"
                ));
            }

            // Filtro por status da propriedade (busca parcial e insensível a maiúsculas/minúsculas)
            if (statusPropriedade != null && !statusPropriedade.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("statusPropriedade").as(String.class)),
                    "%" + statusPropriedade.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
