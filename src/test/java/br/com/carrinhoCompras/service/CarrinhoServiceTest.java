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
import java.math.RoundingMode;
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

        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY),carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar frete de R$10,00, não aplicar desconto de 5% no frete e não aplicar desconto no preço")
    void regra2Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            itens.add(new Item(
                    (long) (i + 1),
                    "livro" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(83.331),
                    0.3166,
                    1,
                    Item.Tipo.LIVRO
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(499.99), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(10.00).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar frete de R$10,00, aplicar desconto de 5% no frete e não aplicar desconto no preço")
    void regra3Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            int quantidade = (i == 2) ? 2 : 1;
            itens.add(new Item(
                    (long) (i + 1),
                    "cadeira" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(83.333),
                    0.33,
                    quantidade,
                    Item.Tipo.COZINHA
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(9.50).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Não cobrar frete, aplicar desconto de 10% no preço e não aplicar desconto no frete")
    void regra4Checkout() {
        Item item = new Item(1L,"celular","descrição", BigDecimal.valueOf(1000),0.1,1, Item.Tipo.ELETRONICO);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        List<Item> itens = new ArrayList<>();
        itens.add(new Item(null,"celular","descrição", BigDecimal.valueOf(1000),0.1,1, Item.Tipo.ELETRONICO));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(900).setScale(2, RoundingMode.UNNECESSARY),carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar frete de R$10,00, aplicar desconto de 10% no preço total e não aplicar desconto de 5% no frete")
    void regra5Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            itens.add(new Item(
                    (long) (i + 1),
                    "camiseta" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(83.35),
                    0.25,
                    1,
                    Item.Tipo.ROUPA
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(450.09).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(10).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar frete de R$10,00, aplicar desconto de 10% no preço total e aplicar desconto de 5% no frete")
    void regra6Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            int quantidade = (i == 2) ? 2 : 1;
            itens.add(new Item(
                    (long) (i + 1),
                    "tinta" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(166.665),
                    0.16,
                    quantidade,
                    Item.Tipo.CASA
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(899.99).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(9.5).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Não cobrar frete, aplicar desconto de 20% no preço total e não aplicar desconto no frete")
    void regra7Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            int quantidade = (i == 2) ? 2 : 1;
            itens.add(new Item(
                    (long) (i + 1),
                    "tinta" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(200.02),
                    0.16,
                    quantidade,
                    Item.Tipo.CASA
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(800.08).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar frete de R$10,00, aplicar desconto de 20% no preço total e não aplicar desconto de 5% no frete")
    void regra8Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            itens.add(new Item(
                    (long) (i + 1),
                    "livro" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(200),
                    0.283,
                    1,
                    Item.Tipo.LIVRO
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(960).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(10).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar frete de R$10,00, aplicar desconto de 20% no preço total e aplicar desconto de 5% no frete")
    void regra9Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            int quantidade = (i == 2) ? 2 : 1;
            itens.add(new Item(
                    (long) (i + 1),
                    "tinta" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(250),
                    0.3,
                    quantidade,
                    Item.Tipo.CASA
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(1200).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(9.5).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, não aplicar desconto de 10% no preço total e não aplicar desconto de 5% no frete")
    void regra10Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            itens.add(new Item(
                    (long) (i + 1),
                    "livro" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(0.025),
                    0.525,
                    1,
                    Item.Tipo.LIVRO
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(0.1).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(4).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }
}