package br.com.carrinhoCompras.repository;

import br.com.carrinhoCompras.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
