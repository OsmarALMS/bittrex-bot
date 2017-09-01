package br.com.bittrexbot.rest.model;

import java.util.List;

public class OrderBook {

	public String success;
	public String message;
	public OrderBookResult result;
	
	public static class OrderBookResult {
		public List<BuyResult> buy;
		public List<SellResult> sell;
	}
	
	public static class BuyResult {
		public double Quantity;
		public double Rate;
	}
	
	public static class SellResult {
		public double Quantity;
		public double Rate;
	}
	
}
