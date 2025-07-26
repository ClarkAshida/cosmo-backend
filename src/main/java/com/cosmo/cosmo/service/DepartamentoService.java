package com.cosmo.cosmo.service;

import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }

    public Departamento findById(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento not found with id: " + id));
    }

    public Departamento save(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    public Departamento update(Long id, Departamento departamentoDetails) {
        Departamento departamento = findById(id);

        departamento.setNome(departamentoDetails.getNome());

        return departamentoRepository.save(departamento);
    }

    public void deleteById(Long id) {
        Departamento departamento = findById(id);
        departamentoRepository.delete(departamento);
    }
}