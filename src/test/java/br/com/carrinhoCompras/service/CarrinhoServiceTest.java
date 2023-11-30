package br.com.carrinhoCompras.service;

import br.com.carrinhoCompras.model.Carrinho;
import br.com.carrinhoCompras.model.Item;
import br.com.carrinhoCompras.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    CarrinhoService carrinhoService;

    @Test
    @DisplayName("Não cobrar frete, nem aplicar desconto no frete e no preço")
    void regra1Checkout() {
        Item item = new Item(1L,"livro","descrição", BigDecimal.ZERO,0,0, Item.Tipo.LIVRO);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        List<Item> itens = new ArrayList<>();
        itens.add(new Item(null,"livro","descrição", BigDecimal.ZERO,0,0, Item.Tipo.LIVRO));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.ZERO.setScale(2),carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.ZERO.setScale(2), carrinho.getFrete());
    }
}