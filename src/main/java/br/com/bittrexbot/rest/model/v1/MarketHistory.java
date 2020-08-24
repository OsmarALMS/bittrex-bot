package br.com.bittrexbot.rest.model.v1;

import java.util.List;
import java.util.TimeZone;

public class MarketHistory {

	public String success;
	public String message;
	public List<MarketHistoryResult> result; 
	
	public static class MarketHistoryResult {
		public String Id;
		public TimeZone TimeStamp;
		public String Quantity;
		public String Price;
		public String Total;
		public String FillType;
		public String OrderType;
	}
	
}
