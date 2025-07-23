package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    /**
     * Busca departamento por nome
     */
    Optional<Departamento> findByNome(String nome);

    /**
     * Busca departamentos por nome (case insensitive e parcial)
     */
    @Query("SELECT d FROM Departamento d WHERE LOWER(d.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Departamento> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Conta usuários por departamento
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.departamento = :departamento")
    long countUsuariosByDepartamento(@Param("departamento") Departamento departamento);

    /**
     * Conta equipamentos por departamento
     */
    @Query("SELECT COUNT(e) FROM Equipamento e WHERE e.departamento = :departamento")
    long countEquipamentosByDepartamento(@Param("departamento") Departamento departamento);
}
