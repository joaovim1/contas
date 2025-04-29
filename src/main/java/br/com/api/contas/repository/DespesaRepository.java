package br.com.api.contas.repository;

import br.com.api.contas.model.*;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d")
    BigDecimal somarDespesas();
}
