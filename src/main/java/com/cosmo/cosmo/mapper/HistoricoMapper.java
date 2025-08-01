package com.cosmo.cosmo.mapper;

import com.cosmo.cosmo.dto.HistoricoRequestDTO;
import com.cosmo.cosmo.dto.HistoricoResponseDTO;
import com.cosmo.cosmo.entity.Historico;
import com.cosmo.cosmo.entity.equipamento.Equipamento;
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
        // Removido campos que não existem no RequestDTO - eles são definidos no service
        historico.setObservacoesEntrega(requestDTO.getObservacoesEntrega());
        historico.setUrlTermoEntrega(requestDTO.getUrlTermoEntrega());
        // Status padrão para novo histórico
        historico.setStatusRegistroHistorico(true);

        return historico;
    }

    public HistoricoResponseDTO toResponseDTO(Historico historico) {
        if (historico == null) {
            return null;
        }

        HistoricoResponseDTO responseDTO = new HistoricoResponseDTO();
        responseDTO.setId(historico.getId());
        responseDTO.setEquipamento(equipamentoMapper.toResponseDTO(historico.getEquipamento()));
        responseDTO.setUsuario(usuarioMapper.toResponseDTO(historico.getUsuario()));
        responseDTO.setDataEntrega(historico.getDataEntrega());
        responseDTO.setDataDevolucao(historico.getDataDevolucao());
        responseDTO.setObservacoesEntrega(historico.getObservacoesEntrega());
        responseDTO.setObservacoesDevolucao(historico.getObservacoesDevolucao());
        responseDTO.setUrlTermoEntrega(historico.getUrlTermoEntrega());
        responseDTO.setUrlTermoDevolucao(historico.getUrlTermoDevolucao());
        responseDTO.setStatusRegistroHistorico(historico.getStatusRegistroHistorico());
        responseDTO.setMotivoCancelamento(historico.getMotivoCancelamento());
        responseDTO.setDataCancelamento(historico.getDataCancelamento());

        return responseDTO;
    }

    public void updateEntityFromDTO(HistoricoRequestDTO requestDTO, Historico historico,
                                   Equipamento equipamento, Usuario usuario) {
        if (requestDTO == null || historico == null) {
            return;
        }

        // Só permite atualizar campos seguros
        historico.setObservacoesEntrega(requestDTO.getObservacoesEntrega());
        historico.setUrlTermoEntrega(requestDTO.getUrlTermoEntrega());
        // Não permite alterar equipamento, usuário ou dados de devolução via DTO
    }
}
