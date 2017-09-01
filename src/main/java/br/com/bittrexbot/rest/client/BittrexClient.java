package br.com.bittrexbot.rest.client;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.bittrexbot.rest.model.BuySellLimit;
import br.com.bittrexbot.rest.model.CancelOrder;
import br.com.bittrexbot.rest.model.MarketHistory;
import br.com.bittrexbot.rest.model.MarketSummary;
import br.com.bittrexbot.rest.model.OpenOrders;
import br.com.bittrexbot.rest.model.OrderBook;
import br.com.bittrexbot.utils.Global;

@Component
public class BittrexClient {

	public MarketSummary getMarketSummaries(){
		String URL = "https://bittrex.com/api/v1.1/public/getmarketsummaries";
		return new RestTemplate().getForObject(URL, MarketSummary.class);
	}
	
	public MarketSummary getMarketSummary(String market){
		String URL = "https://bittrex.com/api/v1.1/public/getmarketsummary?market="+market;
		return new RestTemplate().getForObject(URL, MarketSummary.class);
	}
	
	public OrderBook getOrderBook(String market){
		String URL = "https://bittrex.com/api/v1.1/public/getorderbook?market="+market+"&type=both";
		return new RestTemplate().getForObject(URL, OrderBook.class);
	}
	
	public MarketHistory getMarketHistory(String market){
		String URL = "https://bittrex.com/api/v1.1/public/getmarkethistory?market="+market;
		return new RestTemplate().getForObject(URL, MarketHistory.class);
	}
	
	public BuySellLimit buyLimit(String market, Double quantity, Double orderValue){
		String URL = "https://bittrex.com/api/v1.1/market/buylimit?"
				+ "apikey="+Global.KEY
				+ "&nonce="+String.valueOf(System.currentTimeMillis())
				+ "&market="+market
				+ "&quantity="+quantity
				+ "&rate="+orderValue;	
		return new RestTemplate().exchange(URL, HttpMethod.GET, addApiSignature(URL), BuySellLimit.class).getBody();
	}
	
	public BuySellLimit sellLimit(String market, Double quantity, Double orderValue){
		String URL = "https://bittrex.com/api/v1.1/market/selllimit?"
				+ "apikey="+Global.KEY
				+ "&nonce="+String.valueOf(System.currentTimeMillis())
				+ "&market="+market
				+ "&quantity="+quantity
				+ "&rate="+orderValue;	
		return new RestTemplate().exchange(URL, HttpMethod.GET, addApiSignature(URL), BuySellLimit.class).getBody();
	}
	
	public CancelOrder cancelOrder(String uuid){
		String URL = "https://bittrex.com/api/v1.1/market/cancel?"
				+ "apikey="+Global.KEY
				+ "&nonce="+String.valueOf(System.currentTimeMillis())
				+ "&uuid="+uuid;
		return new RestTemplate().exchange(URL, HttpMethod.GET, addApiSignature(URL), CancelOrder.class).getBody();
	}
	
	public OpenOrders getOpenOrders(String market){
		String URL = "https://bittrex.com/api/v1.1/market/getopenorders?"
				+ "apikey="+Global.KEY
				+ "&nonce="+String.valueOf(System.currentTimeMillis())
				+ "&market="+market;
		return new RestTemplate().exchange(URL, HttpMethod.GET, addApiSignature(URL), OpenOrders.class).getBody();
	}
	
	public HttpEntity<String> addApiSignature(String URL){
		
		String sign = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA512");
			SecretKeySpec secret = new SecretKeySpec(Global.SECRET.getBytes(),"HmacSHA512");
			mac.init(secret);
			byte[] digest = mac.doFinal(URL.getBytes());
			sign = org.apache.commons.codec.binary.Hex.encodeHexString(digest);
		} catch (Exception e) {
			throw new RuntimeException("error on create the apisignature: "+e.getMessage());
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("apisign", sign);
		return new HttpEntity<String>("parameters", headers);
	}
}
