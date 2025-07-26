package com.cosmo.cosmo.service;

import com.cosmo.cosmo.entity.Historico;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    public List<Historico> findAll() {
        return historicoRepository.findAll();
    }

    public Historico findById(Long id) {
        return historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historico not found with id: " + id));
    }

    public Historico save(Historico historico) {
        return historicoRepository.save(historico);
    }

    public Historico update(Long id, Historico historicoDetails) {
        Historico historico = findById(id);

        historico.setEquipamento(historicoDetails.getEquipamento());
        historico.setUsuario(historicoDetails.getUsuario());
        historico.setDataEntrega(historicoDetails.getDataEntrega());
        historico.setDataDevolucao(historicoDetails.getDataDevolucao());
        historico.setObservacoesEntrega(historicoDetails.getObservacoesEntrega());
        historico.setObservacoesDevolucao(historicoDetails.getObservacoesDevolucao());
        historico.setUrlTermoEntrega(historicoDetails.getUrlTermoEntrega());
        historico.setUrlTermoDevolucao(historicoDetails.getUrlTermoDevolucao());

        return historicoRepository.save(historico);
    }

    public void deleteById(Long id) {
        Historico historico = findById(id);
        historicoRepository.delete(historico);
    }

    // Métodos específicos do HistoricoRepository
    public List<Historico> findByUsuarioId(Long usuarioId) {
        return historicoRepository.findByUsuarioId(usuarioId);
    }

    public List<Historico> findByEquipamentoId(Long equipamentoId) {
        return historicoRepository.findByEquipamentoId(equipamentoId);
    }
}