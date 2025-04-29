package br.com.api.contas.service;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.contas.repository.DespesaRepository;
import br.com.api.contas.model.Despesa;

@Service
public class SaldoService {

    private BigDecimal saldo = BigDecimal.ZERO;

    @Autowired
    private DespesaRepository despesaRepository;

    public BigDecimal getSaldo() {
        BigDecimal totalDespesas = despesaRepository.findAll()
                .stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return saldo.subtract(totalDespesas);
    }

    public void adicionarSaldo(BigDecimal valorAdicionado) {
        saldo = saldo.add(valorAdicionado);
    }

    public void removerSaldo(BigDecimal valorRemovido) {
        saldo = saldo.subtract(valorRemovido);
    }

    public void atualizarSaldo(BigDecimal novoSaldo) {
        this.saldo = novoSaldo;
    }

    public void reporSaldo(BigDecimal valor) {
        saldo = saldo.add(valor);
    }
}
