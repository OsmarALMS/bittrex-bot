package br.com.bittrexbot.logic.v3;

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
import br.com.bittrexbot.rest.client.BittrexClientV3;
import br.com.bittrexbot.rest.model.v3.MarketSummary;
import br.com.bittrexbot.utils.Global;

/**
 * @author Osmar Alves Lorente Medina da Silva
 * Main logic V3
 * @see https://github.com/OsmarALMS
 */
@Component
public class LogicBotV3 {

	private static Boolean CLEAN_DATABASE = true;
	
	@Autowired
	private ShoppingRepository shoppingRepository;
	@Autowired
	private ResultRepository resultRepository;
	@Autowired
	private LocalHistoryRepository localHistoryRepository;
	@Autowired
	private BittrexClientV3 client;
	@Autowired
	private LogicLocalHistoryV3 logicLocalHistory;
	@Autowired
	private ShoppingLogicV3 shoppingLogic;
	
	/**
	 * This class is scheduled to run every 60 seconds
	 */
	@Scheduled(cron="*/60 * * * * *")
	public void doTheLogic(){
		
//		client.balances();
		
		if(CLEAN_DATABASE) cleanDataBase();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
		System.out.println(">>>>>>>");
		System.out.println(">>>>>>>   BOT RUN -> "+dateFormat.format(new Date()));
		System.out.println(">>>>>>>");
		
		//Get the purchases made and analyze possible sales
		List<Shopping> listShopping = shoppingRepository.findAll();
		listShopping.forEach(shopping -> {
			
			if(shopping.getSold()) {
				shoppingRepository.delete(shopping);
			}else if(!shopping.getSold()){
				MarketSummary marketSummary = client.getMarketSummary(shopping.getCoin());
				shoppingLogic.sell(marketSummary, shopping);
			}
		});
		
		//Check for possible purchases
		if(!Global.stopProcess()){
			possiblePurchases(Global.logicsv3);
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
				if(l.equals(CoinLogics.LogicLocalHistory)) {
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
			System.out.println(" *****   PROFITS: "+listResult.get(0).getSellProfit() + " --> "+(listResult.get(0).getSellProfit()*Global.PERCENTUAL_PROFIT));
			System.out.println(" *****   LOSSES : "+listResult.get(0).getSellLoss() + " --> "+(listResult.get(0).getSellLoss()*Global.PERCENTUAL_LOSE));
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
