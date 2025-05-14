package br.com.api.contas.repository;

import br.com.api.contas.model.*;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
//QUERYS POSTGREE
       @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d")
    BigDecimal somarDespesas();

    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d WHERE EXTRACT(MONTH FROM d.data) = :mes AND EXTRACT(YEAR FROM d.data) = :ano")
    BigDecimal somarDespesasPorMes(int mes, int ano);

    @Query("SELECT d FROM Despesa d WHERE EXTRACT(MONTH FROM d.data) = :mes AND EXTRACT(YEAR FROM d.data) = :ano")
    List<Despesa> findByMesEAno(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d WHERE LOWER(d.descricao) LIKE '%cigarro%' AND EXTRACT(MONTH FROM d.data) = :mes AND EXTRACT(YEAR FROM d.data) = :ano")
    BigDecimal somarDespesasCigarro(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d WHERE d.pago = true AND EXTRACT(MONTH FROM d.data) = :mes AND EXTRACT(YEAR FROM d.data) = :ano")
    BigDecimal somarDespesasPagas(@Param("mes") int mes, @Param("ano") int ano);

    
 /* @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d")
    BigDecimal somarDespesas();

    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d WHERE FUNCTION('MONTH', d.data) = :mes AND FUNCTION('YEAR', d.data) = :ano")
    BigDecimal somarDespesasPorMes(int mes, int ano);

    @Query("SELECT d FROM Despesa d WHERE FUNCTION('MONTH', d.data) = :mes AND FUNCTION('YEAR', d.data) = :ano")
    List<Despesa> findByMesEAno(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d WHERE LOWER(d.descricao) LIKE '%cigarro%' AND FUNCTION('MONTH', d.data) = :mes AND FUNCTION('YEAR', d.data) = :ano")
    BigDecimal somarDespesasCigarro(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d WHERE d.pago = true AND FUNCTION('MONTH', d.data) = :mes AND FUNCTION('YEAR', d.data) = :ano")
    BigDecimal somarDespesasPagas(@Param("mes") int mes, @Param("ano") int ano);*/

    

}
