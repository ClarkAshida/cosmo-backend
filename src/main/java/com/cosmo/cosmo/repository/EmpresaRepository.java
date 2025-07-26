package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
