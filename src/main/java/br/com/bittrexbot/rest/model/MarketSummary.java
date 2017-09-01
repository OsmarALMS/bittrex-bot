package br.com.bittrexbot.rest.model;

import java.util.List;

public class MarketSummary {

	public String success;
	public String message;
	public List<MarketSummaryResult> result; 
	
	public static class MarketSummaryResult {
		public String MarketName;
		public String High;
		public String Low;
		public String Volume;
		public String Last;
		public String BaseVolume;
		public String TimeStamp;
		public String Bid;
		public String Ask;
		public String OpenBuyOrders;
		public String OpenSellOrders;
		public String PrevDay;
		public String Created;
		public String DisplayMarketName;
	}
	
	public Boolean testExistentMarket(String MarketName) {
		if(result.stream().filter(result -> result.MarketName.equals(MarketName)).count() > 0) {
			return true;
		}
		return false;
	}
	
}
