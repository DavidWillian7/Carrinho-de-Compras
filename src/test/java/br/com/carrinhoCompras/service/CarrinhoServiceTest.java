package br.com.carrinhoCompras.service;

import br.com.carrinhoCompras.model.Carrinho;
import br.com.carrinhoCompras.model.Item;
import br.com.carrinhoCompras.repository.CarrinhoRepository;
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
    CarrinhoRepository carrinhoRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    CarrinhoService carrinhoService;

    @Test
    @DisplayName("Não cobrar frete, nem aplicar desconto no frete e no preço")
    void regra1Checkout() {
        Item item = new Item(1L,"livro","descrição", BigDecimal.valueOf(200),9.9,1, Item.Tipo.LIVRO);
        List<Item> itensProMock = new ArrayList<>();
        itensProMock.add(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        List<Item> itens = new ArrayList<>();
        itens.add(new Item(1L,"livro","descrição", BigDecimal.valueOf(2000),9.9,1, Item.Tipo.LIVRO));

        Carrinho carrinhoProMock = new Carrinho(1L,BigDecimal.valueOf(200),BigDecimal.valueOf(17.10));
        when(carrinhoRepository.save(any(Carrinho.class))).thenReturn(carrinhoProMock);
        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(carrinhoProMock.getItensPrecoTotal(),carrinho.getItensPrecoTotal());
        assertEquals(carrinhoProMock.getFrete(), carrinho.getFrete());
        System.out.println("Preço esperado: " + carrinhoProMock.getItensPrecoTotal() + " Preço retornado: " + carrinho.getItensPrecoTotal());
        System.out.println("Frete esperado: " + carrinhoProMock.getFrete() + " Frete retornado: " + carrinho.getFrete());
    }
}