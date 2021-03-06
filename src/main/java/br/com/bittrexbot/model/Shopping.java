package br.com.bittrexbot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Shopping {

	public Shopping(){}
	
	public Shopping(String coin, Double quantity, Double firstBtcValue, Double btcValue, String orderUuid){
		this.coin = coin;
		this.quantity = quantity;
		this.firstBtcValue = firstBtcValue;
		this.btcValue = btcValue;
		this.orderUuid = orderUuid;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String coin;
	private Double quantity;
	private Double firstBtcValue;
	private Double btcValue;
	private String orderUuid;
	private Boolean sellProfit;
	private Boolean sold = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getBtcValue() {
		return btcValue;
	}

	public void setBtcValue(Double btcValue) {
		this.btcValue = btcValue;
	}

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public Boolean getSellProfit() {
		return sellProfit;
	}

	public void setSellProfit(Boolean sellProfit) {
		this.sellProfit = sellProfit;
	}

	public Boolean getSold() {
		return sold;
	}

	public void setSold(Boolean sold) {
		this.sold = sold;
	}

	public Double getFirstBtcValue() {
		return firstBtcValue;
	}

	public void setFirstBtcValue(Double firstBtcValue) {
		this.firstBtcValue = firstBtcValue;
	}
	
	
}
