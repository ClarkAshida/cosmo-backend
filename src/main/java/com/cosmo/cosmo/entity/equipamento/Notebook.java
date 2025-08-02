package com.cosmo.cosmo.entity.equipamento;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Notebook extends Computador {
}
