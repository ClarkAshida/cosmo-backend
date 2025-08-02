package com.cosmo.cosmo.repository.equipamento;

import com.cosmo.cosmo.entity.equipamento.*;
import com.cosmo.cosmo.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoRepositoryFactory {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private NotebookRepository notebookRepository;

    @Autowired
    private DesktopRepository desktopRepository;

    @Autowired
    private CelularRepository celularRepository;

    @Autowired
    private ChipRepository chipRepository;

    @Autowired
    private ImpressoraRepository impressoraRepository;

    @Autowired
    private MonitorRepository monitorRepository;

    /**
     * Retorna o repositório genérico para operações comuns.
     */
    public EquipamentoRepository getEquipamentoRepository() {
        return equipamentoRepository;
    }

    /**
     * Retorna o repositório específico baseado no tipo de equipamento.
     */
    @SuppressWarnings("unchecked")
    public <T extends Equipamento> org.springframework.data.jpa.repository.JpaRepository<T, Long> getRepositoryByType(Class<T> tipo) {
        if (tipo == Notebook.class) {
            return (org.springframework.data.jpa.repository.JpaRepository<T, Long>) notebookRepository;
        } else if (tipo == Desktop.class) {
            return (org.springframework.data.jpa.repository.JpaRepository<T, Long>) desktopRepository;
        } else if (tipo == Celular.class) {
            return (org.springframework.data.jpa.repository.JpaRepository<T, Long>) celularRepository;
        } else if (tipo == Chip.class) {
            return (org.springframework.data.jpa.repository.JpaRepository<T, Long>) chipRepository;
        } else if (tipo == Impressora.class) {
            return (org.springframework.data.jpa.repository.JpaRepository<T, Long>) impressoraRepository;
        } else if (tipo == Monitor.class) {
            return (org.springframework.data.jpa.repository.JpaRepository<T, Long>) monitorRepository;
        } else {
            throw new IllegalArgumentException("Tipo de equipamento não suportado: " + tipo.getSimpleName());
        }
    }

    /**
     * Métodos específicos para acessar repositórios por tipo.
     */
    public NotebookRepository getNotebookRepository() {
        return notebookRepository;
    }

    public DesktopRepository getDesktopRepository() {
        return desktopRepository;
    }

    public CelularRepository getCelularRepository() {
        return celularRepository;
    }

    public ChipRepository getChipRepository() {
        return chipRepository;
    }

    public ImpressoraRepository getImpressoraRepository() {
        return impressoraRepository;
    }

    public MonitorRepository getMonitorRepository() {
        return monitorRepository;
    }

    /**
     * Verifica se um campo único específico já existe na base de dados.
     */
    public boolean existsUniqueField(Class<? extends Equipamento> tipo, String fieldName, String value, Long excludeId) {
        if (tipo == Celular.class) {
            CelularRepository repo = getCelularRepository();
            switch (fieldName) {
                case "imei":
                    return excludeId != null ? repo.existsByImeiAndIdNot(value, excludeId) : repo.existsByImei(value);
                case "imei2":
                    return excludeId != null ? repo.existsByImei2AndIdNot(value, excludeId) : repo.existsByImei2(value);
                case "eid":
                    return excludeId != null ? repo.existsByEidAndIdNot(value, excludeId) : repo.existsByEid(value);
            }
        } else if (tipo == Chip.class) {
            ChipRepository repo = getChipRepository();
            switch (fieldName) {
                case "iccid":
                    return excludeId != null ? repo.existsByIccidAndIdNot(value, excludeId) : repo.existsByIccid(value);
                case "numeroTelefone":
                    return excludeId != null ? repo.existsByNumeroTelefoneAndIdNot(value, excludeId) : repo.existsByNumeroTelefone(value);
            }
        } else if (tipo == Impressora.class) {
            ImpressoraRepository repo = getImpressoraRepository();
            if ("enderecoIP".equals(fieldName)) {
                return excludeId != null ? repo.existsByEnderecoIPAndIdNot(value, excludeId) : repo.existsByEnderecoIP(value);
            }
        } else if (tipo == Notebook.class || tipo == Desktop.class) {
            if ("hostname".equals(fieldName)) {
                if (tipo == Notebook.class) {
                    NotebookRepository repo = getNotebookRepository();
                    return excludeId != null ? repo.existsByHostnameAndIdNot(value, excludeId) : !repo.findByHostname(value).isEmpty();
                } else {
                    DesktopRepository repo = getDesktopRepository();
                    return excludeId != null ? repo.existsByHostnameAndIdNot(value, excludeId) : !repo.findByHostname(value).isEmpty();
                }
            }
        }

        return false;
    }
}
