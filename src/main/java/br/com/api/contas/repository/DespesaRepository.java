package br.com.api.contas.repository;

import br.com.api.contas.model.*;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d")
    BigDecimal somarDespesas();

    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d WHERE FUNCTION('MONTH', d.data) = :mes AND FUNCTION('YEAR', d.data) = :ano")
    BigDecimal somarDespesasPorMes(int mes, int ano);

    
}
