package com.cosmo.cosmo.specification;

import com.cosmo.cosmo.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
            firstName == null ? null :
            criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                               "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<User> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
            lastName == null ? null :
            criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                               "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
            email == null ? null :
            criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                               "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> hasAccountNonExpired(Boolean accountNonExpired) {
        return (root, query, criteriaBuilder) ->
            accountNonExpired == null ? null :
            criteriaBuilder.equal(root.get("accountNonExpired"), accountNonExpired);
    }

    public static Specification<User> hasAccountNonLocked(Boolean accountNonLocked) {
        return (root, query, criteriaBuilder) ->
            accountNonLocked == null ? null :
            criteriaBuilder.equal(root.get("accountNonLocked"), accountNonLocked);
    }

    public static Specification<User> hasCredentialsNonExpired(Boolean credentialsNonExpired) {
        return (root, query, criteriaBuilder) ->
            credentialsNonExpired == null ? null :
            criteriaBuilder.equal(root.get("credentialsNonExpired"), credentialsNonExpired);
    }

    public static Specification<User> hasEnabled(Boolean enabled) {
        return (root, query, criteriaBuilder) ->
            enabled == null ? null :
            criteriaBuilder.equal(root.get("enabled"), enabled);
    }
}
