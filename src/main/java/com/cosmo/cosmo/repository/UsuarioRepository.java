package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca usuário por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca usuário por CPF
     */
    Optional<Usuario> findByCpf(String cpf);

    /**
     * Busca usuários por departamento
     */
    List<Usuario> findByDepartamento(Departamento departamento);

    /**
     * Busca usuários por departamento com paginação
     */
    Page<Usuario> findByDepartamento(Departamento departamento, Pageable pageable);

    /**
     * Busca usuários por cargo
     */
    List<Usuario> findByCargo(String cargo);

    /**
     * Busca usuários por nome (case insensitive e parcial)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Usuario> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Busca usuários por departamento e cargo
     */
    @Query("SELECT u FROM Usuario u WHERE u.departamento = :departamento AND u.cargo = :cargo")
    List<Usuario> findByDepartamentoAndCargo(@Param("departamento") Departamento departamento,
                                           @Param("cargo") String cargo);

    /**
     * Conta usuários por departamento
     */
    long countByDepartamento(Departamento departamento);
}
