package com.cosmo.cosmo.dto.equipamento;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ImpressoraDetailsDTO {

    private String tipoImpressora;
    private Boolean colorida;
    private Boolean multifuncional;
    private String enderecoIP;
    private String modeloSuprimento;
}
