package com.cosmo.cosmo.mapper;

import com.cosmo.cosmo.dto.HistoricoRequestDTO;
import com.cosmo.cosmo.dto.HistoricoResponseDTO;
import com.cosmo.cosmo.entity.Historico;
import com.cosmo.cosmo.entity.Equipamento;
import com.cosmo.cosmo.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HistoricoMapper {

    @Autowired
    private EquipamentoMapper equipamentoMapper;

    @Autowired
    private UsuarioMapper usuarioMapper;

    public Historico toEntity(HistoricoRequestDTO requestDTO, Equipamento equipamento, Usuario usuario) {
        if (requestDTO == null) {
            return null;
        }

        Historico historico = new Historico();
        historico.setEquipamento(equipamento);
        historico.setUsuario(usuario);
        historico.setDataEntrega(requestDTO.getDataEntrega());
        historico.setDataDevolucao(requestDTO.getDataDevolucao());
        historico.setObservacoesEntrega(requestDTO.getObservacoesEntrega());
        historico.setObservacoesDevolucao(requestDTO.getObservacoesDevolucao());
        historico.setUrlTermoEntrega(requestDTO.getUrlTermoEntrega());
        historico.setUrlTermoDevolucao(requestDTO.getUrlTermoDevolucao());

        return historico;
    }

    public HistoricoResponseDTO toResponseDTO(Historico historico) {
        if (historico == null) {
            return null;
        }

        return new HistoricoResponseDTO(
                historico.getId(),
                equipamentoMapper.toResponseDTO(historico.getEquipamento()),
                usuarioMapper.toResponseDTO(historico.getUsuario()),
                historico.getDataEntrega(),
                historico.getDataDevolucao(),
                historico.getObservacoesEntrega(),
                historico.getObservacoesDevolucao(),
                historico.getUrlTermoEntrega(),
                historico.getUrlTermoDevolucao()
        );
    }

    public void updateEntityFromDTO(HistoricoRequestDTO requestDTO, Historico historico,
                                   Equipamento equipamento, Usuario usuario) {
        if (requestDTO == null || historico == null) {
            return;
        }

        historico.setEquipamento(equipamento);
        historico.setUsuario(usuario);
        historico.setDataEntrega(requestDTO.getDataEntrega());
        historico.setDataDevolucao(requestDTO.getDataDevolucao());
        historico.setObservacoesEntrega(requestDTO.getObservacoesEntrega());
        historico.setObservacoesDevolucao(requestDTO.getObservacoesDevolucao());
        historico.setUrlTermoEntrega(requestDTO.getUrlTermoEntrega());
        historico.setUrlTermoDevolucao(requestDTO.getUrlTermoDevolucao());
    }
}
