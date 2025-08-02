package com.cosmo.cosmo.entity.equipamento;

import com.cosmo.cosmo.enums.StatusPropriedade;
import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class Computador extends Equipamento {

    /**
     * Sistema Operacional instalado (ex: "Windows 11 Pro", "Ubuntu 22.04 LTS").
     */
    private String sistemaOperacional;

    /**
     * Modelo do processador (ex: "Intel Core i7-1185G7").
     */
    private String processador;

    /**
     * Quantidade de memória RAM instalada (ex: "16GB DDR4").
     */
    private String memoriaRAM;

    /**
     * Detalhes do armazenamento principal (ex: "512GB NVMe SSD", "1TB HDD").
     */
    private String armazenamento;

    /**
     * Nome do computador na rede.
     */
    private String hostname;

    private String dominio;

    /**
     * Indica se o acesso remoto (como RDP ou TeamViewer) está habilitado.
     */
    private Boolean remoteAccessEnabled;

    /**
     * Indica se há uma solução de antivírus corporativo instalada e ativa.
     */
    private Boolean antivirusEnabled;

    @Enumerated(EnumType.STRING)
    private StatusPropriedade statusPropriedade;
}
