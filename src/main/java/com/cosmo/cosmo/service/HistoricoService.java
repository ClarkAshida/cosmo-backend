package com.cosmo.cosmo.service;

import com.cosmo.cosmo.dto.HistoricoRequestDTO;
import com.cosmo.cosmo.dto.HistoricoResponseDTO;
import com.cosmo.cosmo.entity.Historico;
import com.cosmo.cosmo.entity.Equipamento;
import com.cosmo.cosmo.entity.Usuario;
import com.cosmo.cosmo.mapper.HistoricoMapper;
import com.cosmo.cosmo.repository.HistoricoRepository;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private HistoricoMapper historicoMapper;

    @Autowired
    private EquipamentoService equipamentoService;

    @Autowired
    private UsuarioService usuarioService;

    public List<HistoricoResponseDTO> findAll() {
        return historicoRepository.findAll()
                .stream()
                .map(historicoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public HistoricoResponseDTO findById(Long id) {
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado com id: " + id));
        return historicoMapper.toResponseDTO(historico);
    }

    public HistoricoResponseDTO save(HistoricoRequestDTO requestDTO) {
        Equipamento equipamento = equipamentoService.findEntityById(requestDTO.getEquipamentoId());
        Usuario usuario = usuarioService.findEntityById(requestDTO.getUsuarioId());

        Historico historico = historicoMapper.toEntity(requestDTO, equipamento, usuario);
        historico = historicoRepository.save(historico);
        return historicoMapper.toResponseDTO(historico);
    }

    public HistoricoResponseDTO update(Long id, HistoricoRequestDTO requestDTO) {
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado com id: " + id));

        Equipamento equipamento = equipamentoService.findEntityById(requestDTO.getEquipamentoId());
        Usuario usuario = usuarioService.findEntityById(requestDTO.getUsuarioId());

        historicoMapper.updateEntityFromDTO(requestDTO, historico, equipamento, usuario);
        historico = historicoRepository.save(historico);
        return historicoMapper.toResponseDTO(historico);
    }

    public void deleteById(Long id) {
        Historico historico = historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado com id: " + id));
        historicoRepository.delete(historico);
    }
}