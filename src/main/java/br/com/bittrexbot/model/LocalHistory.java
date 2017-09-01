package br.com.bittrexbot.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LocalHistory{

	public LocalHistory() {}
	
	public LocalHistory(String marketName, Double lastValue) {
		this.marketName = marketName;
		this.lastValue = lastValue;
		setStrDtAddrow(dateToString());
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String marketName;
	private Double lastValue;
	private String strDtAddrow;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public Double getLastValue() {
		return lastValue;
	}

	public void setLastValue(Double lastValue) {
		this.lastValue = lastValue;
	}
	
	public String getStrDtAddrow() {
		return strDtAddrow;
	}

	public void setStrDtAddrow(String strDtAddrow) {
		this.strDtAddrow = strDtAddrow;
	}
	
	private String dateToString(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf.format(new Date());
	}
}
