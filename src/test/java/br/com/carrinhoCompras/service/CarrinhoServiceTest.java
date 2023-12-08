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

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, não aplicar desconto de 10% no preço total e aplicar desconto de 5% no frete")
    void regra11Checkout() {
        Item item = new Item(1L,"TV","descrição", BigDecimal.valueOf(100),4.95,2, Item.Tipo.ELETRONICO);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        List<Item> itens = new ArrayList<>();
        itens.add(new Item(null,"TV","descrição", BigDecimal.valueOf(100),4.95,2, Item.Tipo.ELETRONICO));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(200).setScale(2, RoundingMode.UNNECESSARY),carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(17.1).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, não aplicar desconto de 10% no preço total, não aplicar desconto de 5% no frete e acrescentar R$10,00 no frete")
    void regra12Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            double peso = (i == 2) ? 1 : 1.8;
            itens.add(new Item(
                    (long) (i + 1),
                    "livro" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(16.666),
                    peso,
                    1,
                    Item.Tipo.LIVRO
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(100).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(30).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, não aplicar desconto de 10% no preço total, aplicar desconto de 5% no frete e acrescentar R$10,00 no frete")
    void regra13Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            int quantidade = (i == 2) ? 2 : 1;
            double peso = (i == 2) ? 1 : 0.8;
            itens.add(new Item(
                    (long) (i + 1),
                    "tinta" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(8.333),
                    peso,
                    quantidade,
                    Item.Tipo.CASA
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(50).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(19).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, aplicar desconto de 10% no preço total e não aplicar desconto de 5% no frete")
    void regra14Checkout() {
        Item item = new Item(1L,"TV","descrição", BigDecimal.valueOf(1000),4,1, Item.Tipo.ELETRONICO);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        List<Item> itens = new ArrayList<>();
        itens.add(new Item(null,"TV","descrição", BigDecimal.valueOf(1000),4,1, Item.Tipo.ELETRONICO));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(900).setScale(2, RoundingMode.UNNECESSARY),carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(8).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, aplicar desconto de 10% no preço total e aplicar desconto de 5% no frete")
    void regra15Checkout() {
        Item item = new Item(1L,"TV","descrição", BigDecimal.valueOf(350),1.5,2, Item.Tipo.ELETRONICO);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        List<Item> itens = new ArrayList<>();
        itens.add(new Item(null,"TV","descrição", BigDecimal.valueOf(350),1.5,2, Item.Tipo.ELETRONICO));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(630).setScale(2, RoundingMode.UNNECESSARY),carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(5.7).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, aplicar desconto de 10% no preço total, não aplicar desconto de 5% no frete e acrescentar R$10,00 no frete")
    void regra16Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            itens.add(new Item(
                    (long) (i + 1),
                    "livro" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(91.666),
                    0.5,
                    1,
                    Item.Tipo.LIVRO
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(495).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(16).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, aplicar desconto de 10% no preço total, aplicar desconto de 5% no frete e acrescentar R$10,00 no frete")
    void regra17Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            int quantidade = (i == 2) ? 2 : 1;
            itens.add(new Item(
                    (long) (i + 1),
                    "tinta" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(100),
                    0.5,
                    quantidade,
                    Item.Tipo.CASA
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(540).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(15.2).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, aplicar desconto de 20% no preço total e não aplicar desconto de 5% no frete")
    void regra18Checkout() {
        Item item = new Item(1L,"TV","descrição", BigDecimal.valueOf(1050),3,1, Item.Tipo.ELETRONICO);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        List<Item> itens = new ArrayList<>();
        itens.add(new Item(null,"TV","descrição", BigDecimal.valueOf(1050),3,1, Item.Tipo.ELETRONICO));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(840).setScale(2, RoundingMode.UNNECESSARY),carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(6).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, aplicar desconto de 20% no preço total e aplicar desconto de 5% no frete")
    void regra19Checkout() {
        Item item = new Item(1L,"TV","descrição", BigDecimal.valueOf(575),2,2, Item.Tipo.ELETRONICO);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        List<Item> itens = new ArrayList<>();
        itens.add(new Item(null,"TV","descrição", BigDecimal.valueOf(575),2,2, Item.Tipo.ELETRONICO));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(920).setScale(2, RoundingMode.UNNECESSARY),carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(7.6).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }

    @Test
    @DisplayName("Cobrar R$2,00 por kg no frete, aplicar desconto de 20% no preço total, não aplicar desconto de 5% no frete e acrescentar R$10,00 no frete")
    void regra20Checkout() {
        List<Item> itens = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            double peso = (i == 2) ? 2 : 1;
            itens.add(new Item(
                    (long) (i + 1),
                    "livro" + (i + 1),
                    "descrição",
                    BigDecimal.valueOf(208.333),
                    peso,
                    1,
                    Item.Tipo.LIVRO
            ));
        }

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrinho carrinho = carrinhoService.checkout(itens);

        assertEquals(BigDecimal.valueOf(1000).setScale(2, RoundingMode.UNNECESSARY), carrinho.getItensPrecoTotal());
        assertEquals(BigDecimal.valueOf(24).setScale(2, RoundingMode.UNNECESSARY), carrinho.getFrete());
    }
}