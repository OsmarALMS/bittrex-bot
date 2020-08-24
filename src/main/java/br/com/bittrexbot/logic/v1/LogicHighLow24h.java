package br.com.bittrexbot.logic.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.bittrexbot.model.Shopping;
import br.com.bittrexbot.repository.ShoppingRepository;
import br.com.bittrexbot.rest.client.BittrexClient;
import br.com.bittrexbot.rest.model.v1.MarketSummary;
import br.com.bittrexbot.utils.Global;

/**
 * @author Osmar
 * This logic is based at the highest and at the lowest value for the last 24h, 
 * you can choose if is better buy coin at a high moment or at a low moment
 */
@Component
public class LogicHighLow24h {
	
	private List<String> disregardCoins = new ArrayList<String>();
	
	@Autowired
	private ShoppingRepository shoppingRepository;
	
	@Autowired
	private BittrexClient client;
	
	/**
	 * @param highValue True if you want buy coin at a high moment / False if you want buy coin at a low moment
	 * @param quantityOfCoins Number of coins to search
	 */
	public List<MarketSummary> doTheLogic(boolean highValue, int quantityOfCoins, List<MarketSummary> testCoins){
		
		List<Shopping> listShopping = shoppingRepository.findAll();
		for(Shopping s : listShopping){
			disregardCoins.add(s.getCoin());
		}
		List<MarketSummary> coins = getCoins(highValue, quantityOfCoins, testCoins);
		if(coins == null || coins.size() == 0){
			System.out.println("<LogicHighLow24h> -- No coin was found...");
			return null;
		}
		return coins;
	}
	
	private List<MarketSummary> getCoins(boolean highValue, int quantityOfCoins, List<MarketSummary> testCoins){
		
		List<MarketSummary> eligibleCoins = new ArrayList<MarketSummary>();
		MarketSummary summaries = client.getMarketSummaries();
		List<CoinsForLogic> listCoins = new ArrayList<CoinsForLogic>();
		
		summaries.result.stream()
			.filter(result -> Double.parseDouble(result.BaseVolume) >= Global.MINIMUN_BASE_VOLUME_TO_BUY)
			.filter(result -> result.MarketName.startsWith("BTC-") && !disregardCoins.contains(result.MarketName))
			.filter(result -> testCoins != null ? (testCoins.stream().filter(s -> s.testExistentMarket(result.MarketName)).count() > 0) : true)
			.forEach(result -> {
				Double lastForHigh = (Double.parseDouble(result.Last) * 100)/Double.parseDouble(result.High);
				Double lastForLow = (Double.parseDouble(result.Last) * 100)/Double.parseDouble(result.Low);
				listCoins.add(new CoinsForLogic(result.MarketName, lastForHigh, lastForLow));
			});
		
		if(highValue){
			listCoins.sort((CoinsForLogic o1, CoinsForLogic o2)-> o2.lastForHigh.compareTo(o1.lastForHigh));
			for(int i = 0; i < quantityOfCoins; i++) {
				System.out.println("<LogicHighLow24h> -- Eligible Coin: "+listCoins.get(i).MarketName);
				eligibleCoins.add(client.getMarketSummary(listCoins.get(i).MarketName));
			}
		}else{
			listCoins.sort((CoinsForLogic o1, CoinsForLogic o2)-> o1.lastForLow.compareTo(o2.lastForLow));
			for(int i = 0; i < quantityOfCoins; i++) {
				System.out.println("<LogicHighLow24h> -- Eligible Coin: "+listCoins.get(i).MarketName);
				eligibleCoins.add(client.getMarketSummary(listCoins.get(i).MarketName));
			}
		}
		return eligibleCoins;
	}
	
	private class CoinsForLogic {
		public CoinsForLogic(String MarketName, Double lastForHigh, Double lastForLow){
			this.MarketName = MarketName;
			this.lastForHigh = lastForHigh;
			this.lastForLow = lastForLow;
		}
		public String MarketName;
		public Double lastForHigh;
		public Double lastForLow;
	}
	
}
