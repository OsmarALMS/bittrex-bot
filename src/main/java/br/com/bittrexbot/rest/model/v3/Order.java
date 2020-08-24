package br.com.bittrexbot.rest.model.v3;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Order {

	public Order(String marketSymbol, Direction direction, Type type, Double quantity, TimeInForce timeInForce) {
		this.marketSymbol = marketSymbol;
		this.direction = direction;
		this.type = type;
		this.quantity = quantity;
		this.timeInForce = timeInForce;
	}
	
	private String marketSymbol;
	private Direction direction;
	private Type type;
	private Double quantity;
	private Double ceiling;
	private Double limit;
	private TimeInForce timeInForce;
	private String clientOrderId = UUID.randomUUID().toString();
	private Boolean useAwards;
	
	public String getMarketSymbol() {
		return marketSymbol;
	}
	public void setMarketSymbol(String marketSymbol) {
		this.marketSymbol = marketSymbol;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getCeiling() {
		return ceiling;
	}
	public void setCeiling(Double ceiling) {
		this.ceiling = ceiling;
	}
	public Double getLimit() {
		return limit;
	}
	public void setLimit(Double limit) {
		this.limit = limit;
	}
	public TimeInForce getTimeInForce() {
		return timeInForce;
	}
	public void setTimeInForce(TimeInForce timeInForce) {
		this.timeInForce = timeInForce;
	}
	public String getClientOrderId() {
		return clientOrderId;
	}
	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}
	public Boolean getUseAwards() {
		return useAwards;
	}
	public void setUseAwards(Boolean useAwards) {
		this.useAwards = useAwards;
	}
	
	public enum Type {
		LIMIT, MARKET, CEILING_LIMIT, CEILING_MARKET
	}
	
	public enum Direction{
		BUY, SELL
	}
	
	public enum TimeInForce{
		GOOD_TIL_CANCELLED, IMMEDIATE_OR_CANCEL, FILL_OR_KILL, POST_ONLY_GOOD_TIL_CANCELLED, BUY_NOW
	}
	
}
