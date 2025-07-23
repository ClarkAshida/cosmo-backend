package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.*;
import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.TipoEquipamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    /**
     * Busca equipamento por número de patrimônio
     */
    Optional<Equipamento> findByNumeroPatrimonio(String numeroPatrimonio);

    /**
     * Busca equipamento por serial number
     */
    Optional<Equipamento> findBySerialNumber(String serialNumber);

    /**
     * Busca equipamento por IMEI
     */
    Optional<Equipamento> findByImei(String imei);

    /**
     * Busca equipamentos por tipo
     */
    List<Equipamento> findByTipoEquipamento(TipoEquipamento tipoEquipamento);

    /**
     * Busca equipamentos por tipo com paginação
     */
    Page<Equipamento> findByTipoEquipamento(TipoEquipamento tipoEquipamento, Pageable pageable);

    /**
     * Busca equipamentos por empresa
     */
    List<Equipamento> findByEmpresa(Empresa empresa);

    /**
     * Busca equipamentos por departamento
     */
    List<Equipamento> findByDepartamento(Departamento departamento);

    /**
     * Busca equipamentos por usuário responsável
     */
    List<Equipamento> findByUsuarioResponsavel(Usuario usuarioResponsavel);

    /**
     * Busca equipamentos por estado de conservação
     */
    List<Equipamento> findByEstadoConservacao(EstadoConservacao estadoConservacao);

    /**
     * Busca equipamentos por marca
     */
    List<Equipamento> findByMarca(String marca);

    /**
     * Busca equipamentos por modelo
     */
    List<Equipamento> findByModelo(String modelo);

    /**
     * Busca equipamentos por marca e modelo
     */
    List<Equipamento> findByMarcaAndModelo(String marca, String modelo);

    /**
     * Busca equipamentos sem usuário responsável
     */
    @Query("SELECT e FROM Equipamento e WHERE e.usuarioResponsavel IS NULL")
    List<Equipamento> findEquipamentosSemResponsavel();

    /**
     * Busca equipamentos com termo de responsabilidade assinado
     */
    List<Equipamento> findByTermoResponsabilidadeTrue();

    /**
     * Busca equipamentos sem termo de responsabilidade
     */
    @Query("SELECT e FROM Equipamento e WHERE e.termoResponsabilidade IS NULL OR e.termoResponsabilidade = false")
    List<Equipamento> findEquipamentosSemTermo();

    /**
     * Busca equipamentos por sistema operacional
     */
    List<Equipamento> findBySistemaOperacional(String sistemaOperacional);

    /**
     * Busca equipamentos com acesso remoto habilitado
     */
    List<Equipamento> findByRemoteAccessEnabledTrue();

    /**
     * Busca equipamentos sem antivírus
     */
    @Query("SELECT e FROM Equipamento e WHERE e.antivirusEnabled IS NULL OR e.antivirusEnabled = false")
    List<Equipamento> findEquipamentosSemAntivirus();

    /**
     * Busca equipamentos por hostname
     */
    Optional<Equipamento> findByHostname(String hostname);

    /**
     * Busca equipamentos por domínio
     */
    List<Equipamento> findByDominio(String dominio);

    /**
     * Busca equipamentos criados em um período
     */
    @Query("SELECT e FROM Equipamento e WHERE e.createdAt BETWEEN :inicio AND :fim")
    List<Equipamento> findByCreatedAtBetween(@Param("inicio") LocalDateTime inicio,
                                           @Param("fim") LocalDateTime fim);

    /**
     * Busca equipamentos atualizados recentemente
     */
    @Query("SELECT e FROM Equipamento e WHERE e.updatedAt >= :dataLimite ORDER BY e.updatedAt DESC")
    List<Equipamento> findRecentlyUpdated(@Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Busca equipamentos por faixa de valor
     */
    @Query("SELECT e FROM Equipamento e WHERE e.valor BETWEEN :valorMinimo AND :valorMaximo")
    List<Equipamento> findByValorBetween(@Param("valorMinimo") Float valorMinimo,
                                       @Param("valorMaximo") Float valorMaximo);

    /**
     * Busca equipamentos por nota fiscal
     */
    Optional<Equipamento> findByNotaFiscal(String notaFiscal);

    /**
     * Filtro complexo para busca de equipamentos
     */
    @Query("SELECT e FROM Equipamento e WHERE " +
           "(:tipoEquipamento IS NULL OR e.tipoEquipamento = :tipoEquipamento) AND " +
           "(:empresa IS NULL OR e.empresa = :empresa) AND " +
           "(:departamento IS NULL OR e.departamento = :departamento) AND " +
           "(:estadoConservacao IS NULL OR e.estadoConservacao = :estadoConservacao) AND " +
           "(:marca IS NULL OR LOWER(e.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
           "(:modelo IS NULL OR LOWER(e.modelo) LIKE LOWER(CONCAT('%', :modelo, '%')))")
    Page<Equipamento> findByFiltros(@Param("tipoEquipamento") TipoEquipamento tipoEquipamento,
                                   @Param("empresa") Empresa empresa,
                                   @Param("departamento") Departamento departamento,
                                   @Param("estadoConservacao") EstadoConservacao estadoConservacao,
                                   @Param("marca") String marca,
                                   @Param("modelo") String modelo,
                                   Pageable pageable);

    /**
     * Estatísticas de equipamentos por estado de conservação
     */
    @Query("SELECT e.estadoConservacao, COUNT(e) FROM Equipamento e GROUP BY e.estadoConservacao")
    List<Object[]> countByEstadoConservacao();

    /**
     * Estatísticas de equipamentos por tipo
     */
    @Query("SELECT e.tipoEquipamento, COUNT(e) FROM Equipamento e GROUP BY e.tipoEquipamento")
    List<Object[]> countByTipoEquipamento();

    /**
     * Estatísticas de equipamentos por empresa
     */
    @Query("SELECT e.empresa.nome, COUNT(e) FROM Equipamento e GROUP BY e.empresa")
    List<Object[]> countByEmpresa();

    /**
     * Valor total dos equipamentos por empresa
     */
    @Query("SELECT e.empresa.nome, SUM(e.valor) FROM Equipamento e WHERE e.valor IS NOT NULL GROUP BY e.empresa")
    List<Object[]> sumValorByEmpresa();
}
