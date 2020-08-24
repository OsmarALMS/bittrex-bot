package br.com.bittrexbot.rest.model.v3;

public class MarketTicker {

	private String symbol;
	private Double lastTradeRate;
	private Double bidRate;
	private Double askRate;
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Double getLastTradeRate() {
		return lastTradeRate;
	}
	public void setLastTradeRate(Double lastTradeRate) {
		this.lastTradeRate = lastTradeRate;
	}
	public Double getBidRate() {
		return bidRate;
	}
	public void setBidRate(Double bidRate) {
		this.bidRate = bidRate;
	}
	public Double getAskRate() {
		return askRate;
	}
	public void setAskRate(Double askRate) {
		this.askRate = askRate;
	}
	

}
