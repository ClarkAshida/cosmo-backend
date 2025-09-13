/*
 * Copyright 2025 Flávio Alexandre Orrico Severiano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cosmo.cosmo.entity;

import com.cosmo.cosmo.entity.equipamento.Equipamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataEntrega;
    @Column(nullable = true)
    private LocalDateTime dataDevolucao;

    @Column(columnDefinition = "TEXT")
    private String observacoesEntrega;
    @Column(columnDefinition = "TEXT")
    private String observacoesDevolucao;

    @Column(nullable = true)
    private String urlTermoEntrega;
    @Column(nullable = true)
    private String urlTermoDevolucao;

    // Novo campo para controle de status do registro
    @Column(nullable = false)
    private Boolean statusRegistroHistorico = true; // true = ativo, false = cancelado

    @Column(columnDefinition = "TEXT")
    private String motivoCancelamento; // Descrição do motivo do cancelamento

    @Column(nullable = true)
    private LocalDateTime dataCancelamento; // Data em que foi cancelado
}
