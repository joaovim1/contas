package br.com.api.contas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.api.contas.model.Despesa;
import br.com.api.contas.repository.DespesaRepository;

@Service
public class DespesaService {

private final DespesaRepository repository;
private final SaldoService saldoService;

public DespesaService(DespesaRepository repository, SaldoService saldoService) {
    this.repository = repository;
    this.saldoService = saldoService;
}

public Despesa salvar(Despesa despesa) {
    saldoService.removerSaldo(despesa.getValor());
    return repository.save(despesa);
    }

public List<Despesa> listarTodas() {
    return repository.findAll();
}

public Optional<Despesa> buscarPorId(Long id) {
    return repository.findById(id);
}

public void excluir(Long id) {
Optional<Despesa> despesaOptional = repository.findById(id);
if (despesaOptional.isPresent()) {
    Despesa despesa = despesaOptional.get();
    saldoService.adicionarSaldo(despesa.getValor());
    repository.deleteById(id);
}
}

public Despesa atualizar(Long id, Despesa novaDespesa) {
Optional<Despesa> despesaOptional = repository.findById(id);
if (despesaOptional.isPresent()) {
    Despesa despesaExistente = despesaOptional.get();
    BigDecimal diferenca = novaDespesa.getValor().subtract(despesaExistente.getValor());

    if (diferenca.signum() > 0) {
        saldoService.removerSaldo(diferenca);
    } else if (diferenca.signum() < 0) {
        saldoService.adicionarSaldo(diferenca.abs());
    }

    despesaExistente.setDescricao(novaDespesa.getDescricao());
    despesaExistente.setValor(novaDespesa.getValor());
    despesaExistente.setData(novaDespesa.getData());

    return repository.save(despesaExistente);
}
    return null;
}

public BigDecimal somarDespesas() {
    return repository.somarDespesas();
}

public List<Despesa> listarPorMesEAno(int mes, int ano) {
    return repository.findByMesEAno(mes, ano);
}

public BigDecimal somarDespesasCigarro(int mes, int ano) {
    return repository.somarDespesasCigarro(mes, ano);
}

public BigDecimal somarDespesasPagasPorMesEAno(int mes, int ano) {
    return repository.somarDespesasPagas(mes, ano);
}

}
