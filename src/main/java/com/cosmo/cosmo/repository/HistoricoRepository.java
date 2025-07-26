package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {


    // Encontrar históricos por usuário
    List<Historico> findByUsuarioId(Long usuarioId);
    // Encontrar históricos por equipamento
    List<Historico> findByEquipamentoId(Long equipamentoId);
}
