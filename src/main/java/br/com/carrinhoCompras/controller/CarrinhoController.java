package br.com.carrinhoCompras.controller;

import br.com.carrinhoCompras.service.CarrinhoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
@AllArgsConstructor
public class CarrinhoController {
    private CarrinhoService carrinhoService;
}
