package com.cosmo.cosmo.repository.equipamento;

import com.cosmo.cosmo.entity.equipamento.Chip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChipRepository extends JpaRepository<Chip, Long> {

    // Métodos para verificar duplicação de campos específicos de Chip na criação
    boolean existsByIccid(String iccid);
    boolean existsByNumeroTelefone(String numeroTelefone);

    // Métodos para verificar duplicação na atualização (excluindo o próprio equipamento)
    boolean existsByIccidAndIdNot(String iccid, Long id);
    boolean existsByNumeroTelefoneAndIdNot(String numeroTelefone, Long id);
}
