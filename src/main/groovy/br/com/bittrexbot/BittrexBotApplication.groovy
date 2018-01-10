package br.com.bittrexbot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BittrexBotApplication {

	static void main(String[] args) {
		SpringApplication.run BittrexBotApplication, args
	}
}
