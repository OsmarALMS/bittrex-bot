package br.com.bittrexbot.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.bittrexbot.model.Shopping;
import br.com.bittrexbot.repository.ShoppingRepository;
import br.com.bittrexbot.rest.client.BittrexClient;
import br.com.bittrexbot.rest.model.MarketSummary;
import br.com.bittrexbot.rest.model.OrderBook;
import br.com.bittrexbot.utils.Global;

/**
 * @author Osmar
 * This logic is based on OrderBook, 
 * coins are selected if the first 20 purchase orders are x times greater than the first 20 orders sales
 */
@Component
public class LogicOrderBook {

	private List<String> disregardCoins = new ArrayList<String>();
	
	@Autowired
	private ShoppingRepository shoppingRepository;
	
	@Autowired
	private BittrexClient client;
	
	/**
	 * @param quantityOfCoins Number of coins to search
	 */
	public List<MarketSummary> doTheLogic(int quantityOfCoins, List<MarketSummary> testCoins){
		
		List<Shopping> listShopping = shoppingRepository.findAll();
		for(Shopping s : listShopping){
			disregardCoins.add(s.getCoin());
		}
		
		List<MarketSummary> eligibleCoins = new ArrayList<MarketSummary>();
		
		MarketSummary summaries = client.getMarketSummaries();
		summaries.result.stream()
			.filter(result -> Double.parseDouble(result.BaseVolume) >= Global.MINIMUN_BASE_VOLUME_TO_BUY)
			.filter(result -> result.MarketName.startsWith("BTC-") && !disregardCoins.contains(result.MarketName))
			.filter(result -> testCoins != null ? (testCoins.stream().filter(s -> s.testExistentMarket(result.MarketName)).count() > 0) : true)
			.forEach(result -> {
				if(eligibleCoins.size() < quantityOfCoins) {
					OrderBook orderbook = client.getOrderBook(result.MarketName);
					Double quantityBuyOrders = orderbook.result.buy.stream().limit(20).map(e -> e.Quantity).reduce(0.0, Double::sum);
					Double quantitySellOrders = orderbook.result.sell.stream().limit(20).map(e -> e.Quantity).reduce(0.0, Double::sum);
					if((quantityBuyOrders/Global.ORDER_BOOK_LOGIC_MULTIPLE) > quantitySellOrders) {
						System.out.println("<LogicOrderBook> -- Eligible Coin: "+result.MarketName);
						eligibleCoins.add(client.getMarketSummary(result.MarketName));
					}
				}
		});
		return eligibleCoins;
	}

}
