package br.com.bittrexbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Osmar Alves Lorente Medina da Silva
 * @see https://github.com/OsmarALMS
 */
@SpringBootApplication
@EnableScheduling
public class ApiBittrexBot {

	public static void main(String[] args) {
		SpringApplication.run(ApiBittrexBot.class, args);
	}
	
}
