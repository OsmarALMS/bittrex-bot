package br.com.bittrexbot.rest.model;

public class Balance {

	public String success;
	public String message;
	public BalanceResult result; 
	
	public static class BalanceResult {
		public String Currency;
		public String Balance;
		public String Available;
		public String Pending;
		public String CryptoAddress;
		public String Requested;
		public String Uuid;
	}
	
}
