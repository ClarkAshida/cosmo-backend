package com.cosmo.cosmo.repository.equipamento;

import com.cosmo.cosmo.entity.equipamento.Celular;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelularRepository extends JpaRepository<Celular, Long> {

    // Métodos para verificar duplicação de campos específicos de Celular na criação
    boolean existsByImei(String imei);
    boolean existsByImei2(String imei2);
    boolean existsByEid(String eid);

    // Métodos para verificar duplicação na atualização (excluindo o próprio equipamento)
    boolean existsByImeiAndIdNot(String imei, Long id);
    boolean existsByImei2AndIdNot(String imei2, Long id);
    boolean existsByEidAndIdNot(String eid, Long id);
}
