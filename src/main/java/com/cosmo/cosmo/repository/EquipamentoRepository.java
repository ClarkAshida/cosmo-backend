package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    // Métodos para verificar duplicação na criação
    boolean existsByNumeroPatrimonio(String numeroPatrimonio);
    boolean existsBySerialNumber(String serialNumber);
    boolean existsByImei(String imei);
    boolean existsByEid(String eid);
    boolean existsByNumeroTelefone(String numeroTelefone);
    boolean existsByIccid(String iccid);

    // Métodos para verificar duplicação na atualização (excluindo o próprio equipamento)
    boolean existsByNumeroPatrimonioAndIdNot(String numeroPatrimonio, Long id);
    boolean existsBySerialNumberAndIdNot(String serialNumber, Long id);
    boolean existsByImeiAndIdNot(String imei, Long id);
    boolean existsByEidAndIdNot(String eid, Long id);
    boolean existsByNumeroTelefoneAndIdNot(String numeroTelefone, Long id);
    boolean existsByIccidAndIdNot(String iccid, Long id);
}
