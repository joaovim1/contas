package br.com.api.contas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.contas.model.Saldo;

@Repository
public interface SaldoRepository extends JpaRepository<Saldo, Long> {
    Optional<Saldo> findById(Long id);
}
