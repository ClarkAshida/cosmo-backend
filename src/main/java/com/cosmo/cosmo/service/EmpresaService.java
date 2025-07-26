package com.cosmo.cosmo.service;

import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    public Empresa findById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa not found with id: " + id));
    }

    public Empresa save(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public Empresa update(Long id, Empresa empresaDetails) {
        Empresa empresa = findById(id);

        empresa.setNome(empresaDetails.getNome());
        empresa.setEstado(empresaDetails.getEstado());

        return empresaRepository.save(empresa);
    }

    public void deleteById(Long id) {
        Empresa empresa = findById(id);
        empresaRepository.delete(empresa);
    }
}