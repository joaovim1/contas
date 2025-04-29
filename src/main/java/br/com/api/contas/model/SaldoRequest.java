package br.com.api.contas.model;

public class SaldoRequest {

    private double valor;

    // Construtores
    public SaldoRequest() {
    }

    public SaldoRequest(double valor) {
        this.valor = valor;
    }

    // Getter e Setter
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
