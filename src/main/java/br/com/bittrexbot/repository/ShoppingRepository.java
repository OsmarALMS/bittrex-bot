package br.com.bittrexbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bittrexbot.model.Shopping;

public interface ShoppingRepository extends JpaRepository<Shopping, Long>{

}
