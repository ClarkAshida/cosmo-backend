package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    /**
     * Busca empresa por nome
     */
    Optional<Empresa> findByNome(String nome);

    /**
     * Busca empresas por estado
     */
    List<Empresa> findByEstado(String estado);

    /**
     * Busca empresas por nome (case insensitive e parcial)
     */
    @Query("SELECT e FROM Empresa e WHERE LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Empresa> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Conta equipamentos por empresa
     */
    @Query("SELECT COUNT(eq) FROM Equipamento eq WHERE eq.empresa = :empresa")
    long countEquipamentosByEmpresa(@Param("empresa") Empresa empresa);

    /**
     * Conta empresas por estado
     */
    long countByEstado(String estado);
}
