package br.com.bittrexbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bittrexbot.model.Result;

public interface ResultRepository extends JpaRepository<Result, Long>{

}
