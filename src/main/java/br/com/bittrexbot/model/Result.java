package br.com.bittrexbot.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Result {

	public Result(){}
	
	public Result(Long id, Long sellProfit, Long sellLoss){
		this.id = id;
		this.sellProfit = sellProfit;
		this.sellLoss = sellLoss;
	}

	@Id
	private Long id;
	private Long sellProfit;
	private Long sellLoss;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSellProfit() {
		return sellProfit;
	}

	public void setSellProfit(Long sellProfit) {
		this.sellProfit = sellProfit;
	}

	public Long getSellLoss() {
		return sellLoss;
	}

	public void setSellLoss(Long sellLoss) {
		this.sellLoss = sellLoss;
	}
	
}
