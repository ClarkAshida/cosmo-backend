package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.Historico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long>, JpaSpecificationExecutor<Historico> {

    // Encontrar históricos por usuário
    List<Historico> findByUsuarioId(Long usuarioId);

    // Encontrar históricos por usuário com paginação
    Page<Historico> findByUsuarioId(Long usuarioId, Pageable pageable);

    // Encontrar históricos por equipamento
    List<Historico> findByEquipamentoId(Long equipamentoId);

    // Encontrar históricos por equipamento com paginação
    Page<Historico> findByEquipamentoId(Long equipamentoId, Pageable pageable);
}
