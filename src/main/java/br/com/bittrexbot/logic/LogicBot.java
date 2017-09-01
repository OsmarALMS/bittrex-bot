package br.com.bittrexbot.logic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.bittrexbot.model.Result;
import br.com.bittrexbot.model.Shopping;
import br.com.bittrexbot.repository.LocalHistoryRepository;
import br.com.bittrexbot.repository.ResultRepository;
import br.com.bittrexbot.repository.ShoppingRepository;
import br.com.bittrexbot.rest.client.BittrexClient;
import br.com.bittrexbot.rest.model.CancelOrder;
import br.com.bittrexbot.rest.model.MarketSummary;
import br.com.bittrexbot.rest.model.OpenOrders;
import br.com.bittrexbot.utils.Global;

/**
 * @author Osmar Alves Lorente Medina da Silva
 * Main logic
 * @see https://github.com/OsmarALMS
 */
@Component
public class LogicBot {

	private static Boolean CLEAN_DATABASE = true;
	
	@Autowired
	private ShoppingRepository shoppingRepository;
	@Autowired
	private ResultRepository resultRepository;
	@Autowired
	private LocalHistoryRepository localHistoryRepository;
	@Autowired
	private BittrexClient client;
	@Autowired
	private LogicHighLow24h logicHighLow24h;
	@Autowired
	private LogicOrderBook logicOrderBook;
	@Autowired
	private LogicLocalHistory logicLocalHistory;
	@Autowired
	private ShoppingLogic shoppingLogic;
	
	/**
	 * This class is scheduled to run every 60 seconds
	 */
	@Scheduled(cron="*/60 * * * * *")
	public void doTheLogic(){
		
		if(CLEAN_DATABASE) cleanDataBase();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
		System.out.println(">>>>>>>");
		System.out.println(">>>>>>>   BOT RUN -> "+dateFormat.format(new Date()));
		System.out.println(">>>>>>>");
		
		//Get the purchases made and analyze possible sales
		List<Shopping> listShopping = shoppingRepository.findAll();
		listShopping.forEach(shopping -> {
			
			//Analyzes pending orders
			OpenOrders openOrders = client.getOpenOrders(shopping.getCoin());
			if(openOrders.result != null && openOrders.result.size() > 0) {
				if(openOrders.result.get(0).OrderType.equals("LIMIT_BUY")) {
					System.out.println("<><><><><> Pending Buy Order for: "+shopping.getCoin());
					//Cancel Order
					CancelOrder cancel = client.cancelOrder(openOrders.result.get(0).OrderUuid);
					if(cancel.success) {
						if(!openOrders.result.get(0).Quantity.equals(openOrders.result.get(0).QuantityRemaining)) {
							//Update values
							shopping.setQuantity(Double.parseDouble(openOrders.result.get(0).QuantityRemaining));
							shoppingRepository.save(shopping);
							System.out.println("<><><><><> Buy Order canceled: "+shopping.getCoin() +" - Parcial quantity!");
						}else {
							shoppingRepository.delete(shopping);
							System.out.println("<><><><><> Buy Order canceled: "+shopping.getCoin());
						}
					}else {
						System.out.println("<><><><><> Problems canceling the buy order: "+cancel.message);
					}
				}else if(openOrders.result.get(0).OrderType.equals("LIMIT_SELL")) {
					System.out.println("<><><><><> Pending Sell Order for: "+shopping.getCoin());
					//Cancel Order
					CancelOrder cancel = client.cancelOrder(openOrders.result.get(0).OrderUuid);
					if(cancel.success) {
						if(!openOrders.result.get(0).Quantity.equals(openOrders.result.get(0).QuantityRemaining)) {
							//Update values
							shopping.setQuantity(Double.parseDouble(openOrders.result.get(0).QuantityRemaining));
							shoppingRepository.save(shopping);
							System.out.println("<><><><><> Sell Order canceled: "+shopping.getCoin()+" - Parcial quantity!");
						}else {
							//Remove from result
							shoppingLogic.updateFinalResult(shopping.getSellProfit(), shopping.getQuantity(), true);
							//Release for new sale
							shopping.setSold(false);
							shoppingRepository.save(shopping);
							//Try to sell again
							MarketSummary marketSummary = client.getMarketSummary(shopping.getCoin());
							shoppingLogic.sell(marketSummary, shopping);
							System.out.println("<><><><><> Sell Order canceled: "+shopping.getCoin());
						}
					}else {
						throw new RuntimeException("<><><><><> Problems canceling the sell order: "+cancel.message);
					}
				}
			}else if(shopping.getSold()) {
				shoppingRepository.delete(shopping);
			}else if(!shopping.getSold()){
				MarketSummary marketSummary = client.getMarketSummary(shopping.getCoin());
				shoppingLogic.sell(marketSummary, shopping);
			}
		});
		
		//Check for possible purchases
		if(!Global.stopProcess()){
			possiblePurchases(Global.logics);
		}else{
			System.out.println("<><><><><> All currencies will be sold, the runtime has expired! ("+Global.STOP_HOUR+"h)");
		}

		//Print Result
		printFinalResult();
	}
	
	private void possiblePurchases(CoinLogics... logics) {
		List<Shopping> listShopping = shoppingRepository.findAll();
		List<MarketSummary> coins = null;
		int quantityOfCoins = Global.AMOUNT_COINS_TO_WORK - listShopping.size();
		if(quantityOfCoins > 0) {
			for(CoinLogics l : logics) {
				if(l.equals(CoinLogics.LogicHighLow24h_HIGH)) {
					coins = logicHighLow24h.doTheLogic(true, quantityOfCoins, coins);
				}else if(l.equals(CoinLogics.LogicHighLow24h_LOW)) {
					coins = logicHighLow24h.doTheLogic(false, quantityOfCoins, coins);
				}else if(l.equals(CoinLogics.LogicOrderBook)) {
					coins = logicOrderBook.doTheLogic(quantityOfCoins, coins);
				}else if(l.equals(CoinLogics.LogicLocalHistory)) {
					coins = logicLocalHistory.doTheLogic(quantityOfCoins, coins);
				}
			}
			if(coins != null && coins.size() > 0) {
				coins.forEach(coin -> shoppingLogic.buy(coin));
			}else {
				System.out.println("<><><><><> Ploblem whit Possible Purchases, no coin was found!");
			}
		}
	}
	
	private void printFinalResult() {
		List<Result> listResult = resultRepository.findAll();
		if(listResult != null && listResult.size() > 0) {
			System.out.println(" ****************************************************** ");
			System.out.println(" *****   PROFITS OF SELLS "+listResult.get(0).getSellProfit());
			System.out.println(" *****   LOSSES  OF SELLS "+listResult.get(0).getSellLoss());
			System.out.println(" ****************************************************** ");
		}
	}
	
	public void cleanDataBase(){
		shoppingRepository.deleteAll();
		resultRepository.deleteAll();
		localHistoryRepository.deleteAll();
		CLEAN_DATABASE = false;
	}
}
