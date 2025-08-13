package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.equipamento.Equipamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long>, JpaSpecificationExecutor<Equipamento> {

    // Métodos para verificar duplicação de campos comuns na criação
    boolean existsByNumeroPatrimonio(String numeroPatrimonio);
    boolean existsBySerialNumber(String serialNumber);

    // Métodos para verificar duplicação de campos comuns na atualização (excluindo o próprio equipamento)
    boolean existsByNumeroPatrimonioAndIdNot(String numeroPatrimonio, Long id);
    boolean existsBySerialNumberAndIdNot(String serialNumber, Long id);

    // Buscar equipamentos por empresa
    List<Equipamento> findByEmpresaId(Long empresaId);

    // Buscar equipamentos por departamento
    List<Equipamento> findByDepartamentoId(Long departamentoId);

    // Buscar equipamentos por tipo específico usando discriminator
    @Query("SELECT e FROM Equipamento e WHERE TYPE(e) = :tipo")
    List<Equipamento> findByTipo(@Param("tipo") Class<? extends Equipamento> tipo);

    // Buscar equipamentos por tipo específico com paginação
    @Query("SELECT e FROM Equipamento e WHERE TYPE(e) = :tipo")
    Page<Equipamento> findByTipo(@Param("tipo") Class<? extends Equipamento> tipo, Pageable pageable);

    // Contar equipamentos por tipo
    @Query("SELECT COUNT(e) FROM Equipamento e WHERE TYPE(e) = :tipo")
    Long countByTipo(@Param("tipo") Class<? extends Equipamento> tipo);
}
