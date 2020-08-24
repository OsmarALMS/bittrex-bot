package br.com.bittrexbot.rest.model.v3;

public class OrderResponse {

	public OrderResponse() {}
	
	public OrderResponse(String id) {
		this.id = id;
	}
	
	private String id;
	private String marketSymbol;
	private String direction;
	private String type;
	private Double quantity;
	private Double limit;
	private Double ceiling;
	private String timeInForce;
	private String clientOrderId;
	private Double fillQuantity;
	private Double commission;
	private Double proceeds;
	private String status;
	private String createdAt;
	private String updatedAt;
	private String closedAt;
	private OrderToCancel orderToCancel;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMarketSymbol() {
		return marketSymbol;
	}
	public void setMarketSymbol(String marketSymbol) {
		this.marketSymbol = marketSymbol;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getLimit() {
		return limit;
	}
	public void setLimit(Double limit) {
		this.limit = limit;
	}
	public Double getCeiling() {
		return ceiling;
	}
	public void setCeiling(Double ceiling) {
		this.ceiling = ceiling;
	}
	public String getTimeInForce() {
		return timeInForce;
	}
	public void setTimeInForce(String timeInForce) {
		this.timeInForce = timeInForce;
	}
	public String getClientOrderId() {
		return clientOrderId;
	}
	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}
	public Double getFillQuantity() {
		return fillQuantity;
	}
	public void setFillQuantity(Double fillQuantity) {
		this.fillQuantity = fillQuantity;
	}
	public Double getCommission() {
		return commission;
	}
	public void setCommission(Double commission) {
		this.commission = commission;
	}
	public Double getProceeds() {
		return proceeds;
	}
	public void setProceeds(Double proceeds) {
		this.proceeds = proceeds;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getClosedAt() {
		return closedAt;
	}
	public void setClosedAt(String closedAt) {
		this.closedAt = closedAt;
	}
	public OrderToCancel getOrderToCancel() {
		return orderToCancel;
	}
	public void setOrderToCancel(OrderToCancel orderToCancel) {
		this.orderToCancel = orderToCancel;
	}

	public class OrderToCancel{
		
		private String type;
		private String id;
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	}
}
