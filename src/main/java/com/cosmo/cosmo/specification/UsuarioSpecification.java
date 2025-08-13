package com.cosmo.cosmo.specification;

import com.cosmo.cosmo.entity.Usuario;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSpecification {

    public static Specification<Usuario> comFiltros(String nome, String email, String cpf) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por nome (busca parcial e insensível a maiúsculas/minúsculas)
            if (nome != null && !nome.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nome")),
                    "%" + nome.toLowerCase() + "%"
                ));
            }

            // Filtro por email (busca parcial e insensível a maiúsculas/minúsculas)
            if (email != null && !email.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%"
                ));
            }

            // Filtro por CPF (busca parcial)
            if (cpf != null && !cpf.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    root.get("cpf"),
                    "%" + cpf + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
