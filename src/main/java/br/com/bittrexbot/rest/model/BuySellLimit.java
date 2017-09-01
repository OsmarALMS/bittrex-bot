package br.com.bittrexbot.rest.model;

public class BuySellLimit {

	public BuySellLimit(){}
	
	public BuySellLimit(Boolean success){
		this.success = success;
		this.result = new BuySellLimitResult();
		this.result.uuid = "00000000000000000";
	}
	
	public Boolean success;
	public String message;
	public BuySellLimitResult result; 
	
	public static class BuySellLimitResult {
		public String uuid;
	}
	
}
