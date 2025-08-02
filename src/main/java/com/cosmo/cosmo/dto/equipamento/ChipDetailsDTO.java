package com.cosmo.cosmo.dto.equipamento;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChipDetailsDTO {

    private String numeroTelefone;
    private String iccid;
    private String operadora;
    private String tipoPlano;
}
