package com.cosmo.cosmo.dto.historico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoHistoricoDTO {

    @NotBlank(message = "Motivo do cancelamento é obrigatório")
    @Size(max = 1000, message = "Motivo do cancelamento deve ter no máximo 1000 caracteres")
    private String motivoCancelamento;
}
