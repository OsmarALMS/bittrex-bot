package br.com.bittrexbot.rest.client;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.hash.Hashing;

import br.com.bittrexbot.rest.model.v3.MarketSummary;
import br.com.bittrexbot.rest.model.v3.MarketTicker;
import br.com.bittrexbot.rest.model.v3.Order;
import br.com.bittrexbot.rest.model.v3.Order.Direction;
import br.com.bittrexbot.rest.model.v3.Order.TimeInForce;
import br.com.bittrexbot.rest.model.v3.Order.Type;
import br.com.bittrexbot.rest.model.v3.OrderResponse;
import br.com.bittrexbot.utils.Global;

@Component
public class BittrexClientV3 {

	public MarketSummary[] getMarketSummaries(){
		String URL = "https://api.bittrex.com/v3/markets/summaries";
		return new RestTemplate().getForObject(URL, MarketSummary[].class);
	}
	
	public MarketSummary getMarketSummary(String market){
		String URL = "https://api.bittrex.com/v3/markets/"+market+"/summary";
		return new RestTemplate().getForObject(URL, MarketSummary.class);
	}
	
	public MarketTicker getMarketTicker(String market){
		String URL = "https://api.bittrex.com/v3/markets/"+market+"/ticker";
		return new RestTemplate().getForObject(URL, MarketTicker.class);
	}
	
	public OrderResponse order(String market, Direction direction, Double quantity) {
		//Market Order ONLY
		String URL = "https://api.bittrex.com/v3/orders";
		return new RestTemplate().postForObject(URL, new Order(market, direction, Type.MARKET, quantity, TimeInForce.IMMEDIATE_OR_CANCEL), OrderResponse.class);
	}
	
	public HttpEntity<String> addApiSignature(String URL, Object content, HttpMethod httpMethod){
		
		try {
			
			String contentHash = "";
			if(content != null) {
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(content);
				contentHash = Hashing.sha512().hashString(json, StandardCharsets.UTF_8).toString();
			}
			
			String timestamp = String.valueOf(new Date().getTime());
			
			String preSign = timestamp+URL+httpMethod.name()+contentHash+"";
			Mac mac = Mac.getInstance("HmacSHA512");
			SecretKeySpec secret = new SecretKeySpec(Global.SECRET.getBytes(),"HmacSHA512");
			mac.init(secret);
			byte[] digest = mac.doFinal(preSign.getBytes());
			String sign = org.apache.commons.codec.binary.Hex.encodeHexString(digest);
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Api-Key", Global.KEY);
			headers.add("Api-Timestamp", timestamp);
			headers.add("Api-Content-Hash", contentHash);
			headers.add("Api-Signature", contentHash);
			headers.add("apisign", sign);
			
			return new HttpEntity<String>("parameters", headers);
			
		} catch (Exception e) {
			throw new RuntimeException("error on create the apisignature: "+e.getMessage());
		}
	}
}
