package com.cosmo.cosmo.service;

import com.cosmo.cosmo.entity.Equipamento;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    public List<Equipamento> findAll() {
        return equipamentoRepository.findAll();
    }

    public Equipamento findById(Long id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento not found with id: " + id));
    }

    public Equipamento save(Equipamento equipamento) {
        return equipamentoRepository.save(equipamento);
    }

    public Equipamento update(Long id, Equipamento equipamentoDetails) {
        Equipamento equipamento = findById(id);

        equipamento.setNumeroPatrimonio(equipamentoDetails.getNumeroPatrimonio());
        equipamento.setSerialNumber(equipamentoDetails.getSerialNumber());
        equipamento.setImei(equipamentoDetails.getImei());
        equipamento.setTipoEquipamento(equipamentoDetails.getTipoEquipamento());
        equipamento.setMarca(equipamentoDetails.getMarca());
        equipamento.setModelo(equipamentoDetails.getModelo());
        equipamento.setEstadoConservacao(equipamentoDetails.getEstadoConservacao());
        equipamento.setTermoResponsabilidade(equipamentoDetails.getTermoResponsabilidade());
        equipamento.setEmpresa(equipamentoDetails.getEmpresa());
        equipamento.setSiglaEstado(equipamentoDetails.getSiglaEstado());
        equipamento.setDepartamento(equipamentoDetails.getDepartamento());
        equipamento.setValor(equipamentoDetails.getValor());
        equipamento.setNotaFiscal(equipamentoDetails.getNotaFiscal());
        equipamento.setObservacoes(equipamentoDetails.getObservacoes());
        equipamento.setStatus(equipamentoDetails.getStatus());

        return equipamentoRepository.save(equipamento);
    }

    public void deleteById(Long id) {
        Equipamento equipamento = findById(id);
        equipamentoRepository.delete(equipamento);
    }
}
