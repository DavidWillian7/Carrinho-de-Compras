package br.com.carrinhoCompras.repository;

import br.com.carrinhoCompras.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
}
