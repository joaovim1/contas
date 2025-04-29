package br.com.api.contas.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.contas.model.Despesa;
import br.com.api.contas.model.SaldoRequest;
import br.com.api.contas.service.DespesaService;
import br.com.api.contas.service.SaldoService;

@RestController
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;
    private final SaldoService saldoService;

    public DespesaController(DespesaService despesaService, SaldoService saldoService) {
        this.despesaService = despesaService;
        this.saldoService = saldoService;
    }

    @PostMapping
    public ResponseEntity<Despesa> criar(@RequestBody Despesa despesa) {
        BigDecimal valorDespesa = despesa.getValor();
        saldoService.removerSaldo(valorDespesa);
        return ResponseEntity.ok(despesaService.salvar(despesa));
    }

    @GetMapping
    public List<Despesa> listar() {
        return despesaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despesa> buscarPorId(@PathVariable Long id) {
        return despesaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Despesa> atualizar(@PathVariable Long id, @RequestBody Despesa novaDespesa) {
        return despesaService.buscarPorId(id)
                .map(despesaAntiga -> {
                    BigDecimal valorAntigo = despesaAntiga.getValor();
                    BigDecimal valorNovo = novaDespesa.getValor();
                    BigDecimal diferenca = valorNovo.subtract(valorAntigo);
                    saldoService.removerSaldo(diferenca);
                    novaDespesa.setId(despesaAntiga.getId());
                    return ResponseEntity.ok(despesaService.salvar(novaDespesa));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluir(@PathVariable Long id) {
        return despesaService.buscarPorId(id)
                .map(despesa -> {
                    saldoService.reporSaldo(despesa.getValor());
                    despesaService.excluir(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> obterSaldo() {
        return ResponseEntity.ok(saldoService.getSaldo());
    }

    @PostMapping("/saldo")
    public ResponseEntity<Void> adicionarSaldo(@RequestBody SaldoRequest saldoRequest) {
        BigDecimal valor = BigDecimal.valueOf(saldoRequest.getValor());
        saldoService.adicionarSaldo(valor);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/saldo/remover")
    public ResponseEntity<Void> removerSaldo(@RequestBody SaldoRequest saldoRequest) {
        BigDecimal valor = BigDecimal.valueOf(saldoRequest.getValor());
        saldoService.removerSaldo(valor);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/saldo")
    public ResponseEntity<Void> atualizarSaldo(@RequestBody SaldoRequest saldoRequest) {
        BigDecimal valor = BigDecimal.valueOf(saldoRequest.getValor());
        saldoService.atualizarSaldo(valor);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> obterTotalDespesas() {
        return ResponseEntity.ok(despesaService.somarDespesas());
    }

    @GetMapping("/total/{mes}/{ano}")
    public ResponseEntity<BigDecimal> obterTotalDespesasPorMes(@PathVariable int mes, @PathVariable int ano) {
        BigDecimal total = despesaService.obterDespesasPorMes(mes, ano);
        return ResponseEntity.ok(total);
    }

}
