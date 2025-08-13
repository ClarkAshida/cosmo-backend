package com.cosmo.cosmo.specification;

import com.cosmo.cosmo.entity.Historico;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoricoSpecification {

    public static Specification<Historico> comFiltros(
            Long usuarioId,
            Long equipamentoId,
            LocalDate dataEntregaInicio,
            LocalDate dataEntregaFim,
            LocalDate dataDevolucaoInicio,
            LocalDate dataDevolucaoFim,
            String statusRegistroHistorico) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por usuário ID
            if (usuarioId != null) {
                predicates.add(criteriaBuilder.equal(root.get("usuario").get("id"), usuarioId));
            }

            // Filtro por equipamento ID
            if (equipamentoId != null) {
                predicates.add(criteriaBuilder.equal(root.get("equipamento").get("id"), equipamentoId));
            }

            // Filtro por data de entrega - início
            if (dataEntregaInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataEntrega"), dataEntregaInicio));
            }

            // Filtro por data de entrega - fim
            if (dataEntregaFim != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataEntrega"), dataEntregaFim));
            }

            // Filtro por data de devolução - início
            if (dataDevolucaoInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataDevolucao"), dataDevolucaoInicio));
            }

            // Filtro por data de devolução - fim
            if (dataDevolucaoFim != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataDevolucao"), dataDevolucaoFim));
            }

            // Filtro por status do registro histórico (busca parcial e insensível a maiúsculas/minúsculas)
            if (statusRegistroHistorico != null && !statusRegistroHistorico.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("statusRegistroHistorico").as(String.class)),
                    "%" + statusRegistroHistorico.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
