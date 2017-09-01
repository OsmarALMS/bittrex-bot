package br.com.bittrexbot.rest.model;

import java.util.List;

public class OpenOrders {

	public String success;
	public String message;
	public List<OpenOrdersResult> result; 
	
	public static class OpenOrdersResult {
		public String Uuid;
		public String OrderUuid;
		public String Exchange;
		public String OrderType;
		public String Quantity;
		public String QuantityRemaining;
		public String Limit;
		public String CommissionPaid;
		public String Price;
		public String PricePerUnit;
		public String Opened;
		public String Closed;
		public String CancelInitiated;
		public String ImmediateOrCancel;
		public String IsConditional;
		public String Condition;
		public String ConditionTarget;
	}
	
}
