package com.cosmo.cosmo.dto.equipamento;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CelularDetailsDTO {

    private String imei;
    private String imei2;
    private String eid;
    private Boolean gerenciadoPorMDM;
    private String MDM;
}
