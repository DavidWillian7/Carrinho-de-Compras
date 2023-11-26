package br.com.carrinhoCompras.controller;

import br.com.carrinhoCompras.model.Carrinho;
import br.com.carrinhoCompras.model.Item;
import br.com.carrinhoCompras.service.CarrinhoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@AllArgsConstructor
public class CarrinhoController {
    private CarrinhoService carrinhoService;

    @PostMapping("/checkout")
    public ResponseEntity<Carrinho> checkout(@RequestBody List<Item> itens){
        Carrinho response = carrinhoService.checkout(itens);
        return ResponseEntity.ok(response);
    }
}
