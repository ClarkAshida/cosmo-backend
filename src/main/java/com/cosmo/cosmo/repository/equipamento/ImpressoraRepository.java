package com.cosmo.cosmo.repository.equipamento;

import com.cosmo.cosmo.entity.equipamento.Impressora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImpressoraRepository extends JpaRepository<Impressora, Long> {

    // Buscar impressoras por tipo
    List<Impressora> findByTipoImpressora(String tipoImpressora);

    // Buscar impressoras por endereço IP
    List<Impressora> findByEnderecoIP(String enderecoIP);

    // Verificar se endereço IP já existe
    boolean existsByEnderecoIP(String enderecoIP);

    // Verificar se endereço IP já existe (excluindo o próprio equipamento)
    boolean existsByEnderecoIPAndIdNot(String enderecoIP, Long id);

    // Buscar apenas impressoras coloridas
    @Query("SELECT i FROM Impressora i WHERE i.colorida = true")
    List<Impressora> findColoridas();

    // Buscar apenas impressoras multifuncionais
    @Query("SELECT i FROM Impressora i WHERE i.multifuncional = true")
    List<Impressora> findMultifuncionais();

    // Buscar por modelo de suprimento
    List<Impressora> findByModeloSuprimento(String modeloSuprimento);
}
