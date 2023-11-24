package br.com.carrinhoCompras.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class Item {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private double peso;
    private int quantidade;
    private Tipo tipo;

    public enum Tipo{
        CASA,
        COZINHA,
        ELETRONICO,
        LIVRO,
        ROUPA
    }
}
