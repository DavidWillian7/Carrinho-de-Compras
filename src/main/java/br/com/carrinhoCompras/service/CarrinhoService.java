package br.com.carrinhoCompras.service;

import br.com.carrinhoCompras.model.Carrinho;
import br.com.carrinhoCompras.model.Item;
import br.com.carrinhoCompras.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CarrinhoService {
    private final ItemRepository itemRepository;

    public CarrinhoService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Carrinho checkout(List<Item> itens) {
        this.salvarItens(itens);
        BigDecimal valorFrete = this.calcularFrete(itens);
        BigDecimal descontoFrete = this.aplicarDescontoFrete(itens).multiply(valorFrete);
        valorFrete = valorFrete.subtract(descontoFrete);
        valorFrete = valorFrete.setScale(2, RoundingMode.HALF_UP);
        Carrinho carrinho = new Carrinho();
        carrinho.setFrete(valorFrete);
        BigDecimal precoTotal = this.calcularPrecoTotal(itens);
        BigDecimal descontoPreco = this.aplicarDescontoPreco(precoTotal);
        precoTotal = precoTotal.subtract(descontoPreco);
        precoTotal = precoTotal.setScale(2, RoundingMode.HALF_UP);
        carrinho.setItensPrecoTotal(precoTotal);
        return carrinho;
    }

    private BigDecimal calcularPrecoTotal(List<Item> itens) {
        BigDecimal total = BigDecimal.ZERO;

        for(Item item : itens){
            if(item.getQuantidade() > 1){
                BigDecimal aux = item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
                total = total.add(aux);
            }else{
                total = total.add(item.getPreco());
            }
        }

        return total;
    }

    private BigDecimal calcularFrete(List<Item> itens) {
        BigDecimal valor = BigDecimal.ZERO;
        if(itens.size() >= 5){
            valor = valor.add(BigDecimal.valueOf(10));
        }

        double peso = 0;

        for(Item item : itens){
            if(item.getQuantidade() > 1){
                peso+= item.getPeso() * item.getQuantidade();
            }else{
                peso+= item.getPeso();
            }
        }

        BigDecimal auxPeso = BigDecimal.valueOf(peso);
        int parteInteiraPeso = auxPeso.intValue();
        if(peso > 2.0 && peso <= 10.0){
            BigDecimal aux = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(parteInteiraPeso));
            valor = valor.add(aux);
        }else if(peso > 10.0 && peso <= 50.0){
            BigDecimal aux = BigDecimal.valueOf(4).multiply(BigDecimal.valueOf(parteInteiraPeso));
            valor = valor.add(aux);
        }else if(peso > 50.0){
            BigDecimal aux = BigDecimal.valueOf(7).multiply(BigDecimal.valueOf(parteInteiraPeso));
            valor = valor.add(aux);
        }

        return valor;
    }

    private BigDecimal aplicarDescontoFrete(List<Item> itens) {
        BigDecimal desconto = BigDecimal.ZERO;
        for(Item item : itens){
            if(item.getQuantidade() >= 2){
                desconto = BigDecimal.valueOf(0.05);
                return desconto;
            }
        }
        return desconto;
    }

    private BigDecimal aplicarDescontoPreco(BigDecimal valorTotal){
        BigDecimal desconto = BigDecimal.ZERO;
        if(valorTotal.compareTo(BigDecimal.valueOf(500)) > 0 && valorTotal.compareTo(BigDecimal.valueOf(1000)) <= 0){
            desconto = desconto.add(BigDecimal.valueOf(0.1).multiply(valorTotal));
        }else if(valorTotal.compareTo(BigDecimal.valueOf(1000)) > 0){
            desconto = desconto.add(BigDecimal.valueOf(0.2).multiply(valorTotal));
        }
        return desconto;
    }

    private void salvarItens(List<Item> itens){
        for(Item item : itens){
            itemRepository.save(item);
        }
    }
}
