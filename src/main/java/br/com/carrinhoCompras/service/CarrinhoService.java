package br.com.carrinhoCompras.service;

import br.com.carrinhoCompras.repository.CarrinhoRepository;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoService {
    private CarrinhoRepository carrinhoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository) {
        this.carrinhoRepository = carrinhoRepository;
    }
}
