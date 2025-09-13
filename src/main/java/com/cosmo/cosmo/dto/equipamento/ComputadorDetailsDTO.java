package com.cosmo.cosmo.dto.equipamento;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ComputadorDetailsDTO {

    private String sistemaOperacional;
    private String processador;
    private String memoriaRAM;
    private String armazenamento;
    private String hostname;
    private String dominio;
    private Boolean remoteAccessEnabled;
    private Boolean antivirusEnabled;
}
