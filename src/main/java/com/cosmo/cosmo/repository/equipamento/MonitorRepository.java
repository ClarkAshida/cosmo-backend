package com.cosmo.cosmo.repository.equipamento;

import com.cosmo.cosmo.entity.equipamento.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {

    // Buscar monitores por tamanho da tela
    List<Monitor> findByTamanhoTela(Double tamanhoTela);

    // Buscar monitores por resolução
    List<Monitor> findByResolucao(String resolucao);

    // Buscar monitores por faixa de tamanho
    @Query("SELECT m FROM Monitor m WHERE m.tamanhoTela BETWEEN :tamanhoMin AND :tamanhoMax")
    List<Monitor> findByTamanhoTelaRange(@Param("tamanhoMin") Double tamanhoMin, @Param("tamanhoMax") Double tamanhoMax);

    // Buscar monitores grandes (acima de 24 polegadas)
    @Query("SELECT m FROM Monitor m WHERE m.tamanhoTela > 24.0")
    List<Monitor> findMonitoresGrandes();

    // Buscar monitores com resolução 4K
    @Query("SELECT m FROM Monitor m WHERE m.resolucao LIKE '%3840x2160%' OR m.resolucao LIKE '%4K%'")
    List<Monitor> findMonitores4K();
}
