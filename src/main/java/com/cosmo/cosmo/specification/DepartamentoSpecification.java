package com.cosmo.cosmo.specification;

import com.cosmo.cosmo.entity.Departamento;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoSpecification {

    public static Specification<Departamento> comFiltros(String nome) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por nome (busca parcial e insensível a maiúsculas/minúsculas)
            if (nome != null && !nome.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nome")),
                    "%" + nome.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
