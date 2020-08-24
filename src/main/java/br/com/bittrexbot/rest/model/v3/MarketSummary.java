package br.com.bittrexbot.rest.model.v3;

public class MarketSummary {


	private String symbol;
	private Double high;
	private Double low;
	private Double volume;
	private Double quoteVolume;
	private Double percentChange;
	private String updatedAt;
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Double getHigh() {
		return high;
	}
	public void setHigh(Double high) {
		this.high = high;
	}
	public Double getLow() {
		return low;
	}
	public void setLow(Double low) {
		this.low = low;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public Double getQuoteVolume() {
		return quoteVolume;
	}
	public void setQuoteVolume(Double quoteVolume) {
		this.quoteVolume = quoteVolume;
	}
	public Double getPercentChange() {
		return percentChange;
	}
	public void setPercentChange(Double percentChange) {
		this.percentChange = percentChange;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
}
