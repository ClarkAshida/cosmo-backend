package com.cosmo.cosmo.repository.equipamento;

import com.cosmo.cosmo.entity.equipamento.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotebookRepository extends JpaRepository<Notebook, Long> {

    // Buscar notebooks por hostname
    List<Notebook> findByHostname(String hostname);

    // Buscar notebooks por sistema operacional
    List<Notebook> findBySistemaOperacional(String sistemaOperacional);

    // Verificar se hostname já existe (excluindo o próprio equipamento)
    boolean existsByHostnameAndIdNot(String hostname, Long id);

    // Buscar notebooks com acesso remoto habilitado
    @Query("SELECT n FROM Notebook n WHERE n.remoteAccessEnabled = true")
    List<Notebook> findWithRemoteAccessEnabled();

    // Buscar notebooks sem antivírus
    @Query("SELECT n FROM Notebook n WHERE n.antivirusEnabled = false OR n.antivirusEnabled IS NULL")
    List<Notebook> findWithoutAntivirus();
}
