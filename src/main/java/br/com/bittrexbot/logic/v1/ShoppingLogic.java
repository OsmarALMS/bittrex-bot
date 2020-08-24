package br.com.bittrexbot.logic.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.bittrexbot.model.Result;
import br.com.bittrexbot.model.Shopping;
import br.com.bittrexbot.repository.ResultRepository;
import br.com.bittrexbot.repository.ShoppingRepository;
import br.com.bittrexbot.rest.client.BittrexClient;
import br.com.bittrexbot.rest.model.v1.BuySellLimit;
import br.com.bittrexbot.rest.model.v1.MarketSummary;
import br.com.bittrexbot.utils.Global;

/**
 * @author Osmar
 * This class is responsible to buy and sell
 *
 */
@Component
public class ShoppingLogic {
	
	@Autowired
	private BittrexClient client;
	
	@Autowired
	private ShoppingRepository shoppingRepository;
	@Autowired
	private ResultRepository resultRepository;

	/**
	 * Buy Method
	 * @param marketSummaryCoin
	 */
	public void buy(MarketSummary marketSummaryCoin) {
		Double quantity = quantityForBuy(Double.parseDouble(marketSummaryCoin.result.get(0).Bid));
		System.out.println("<ShoppingLogic> -- Buying "+quantity+" "+marketSummaryCoin.result.get(0).MarketName+" Price: "+Double.parseDouble(marketSummaryCoin.result.get(0).Bid));
		BuySellLimit buyConfirmed = 
				!Global.SIMULATION ? client.buyLimit(marketSummaryCoin.result.get(0).MarketName, quantity, Double.parseDouble(marketSummaryCoin.result.get(0).Bid))
						    : new BuySellLimit(true);
		
		if(buyConfirmed.success){
			System.out.println("<ShoppingLogic> -- Buy Order confirmed");
			shoppingRepository.save(
					new Shopping(marketSummaryCoin.result.get(0).MarketName, quantity,
							Double.parseDouble(marketSummaryCoin.result.get(0).Bid),
							Double.parseDouble(marketSummaryCoin.result.get(0).Bid), buyConfirmed.result.uuid));
		}else{
			throw new RuntimeException("<ShoppingLogic> -- Problems with Buy Order: "+buyConfirmed.message);
		}
	}
	
	/**
	 * Sell Method
	 * @param marketSummary
	 * @param shopping
	 * @param simulation
	 */
	public void sell(MarketSummary marketSummary, Shopping shopping) {
		Double valueForSell = Double.parseDouble(marketSummary.result.get(0).Ask) * shopping.getQuantity();
		
		System.out.println("<ShoppingLogic> Checking coin: "+shopping.getCoin()+" Ask: "+marketSummary.result.get(0).Ask+" Buyed at: "+shopping.getBtcValue());
		
		if(Global.stopProcess()){
			System.out.println("<ShoppingLogic> Sell coin: "+shopping.getCoin() +" --- STOP PROCESS");
			sellCoin(false, shopping, marketSummary);
		}else if(valueForSell >= calcProfitValue(shopping.getQuantity(), shopping.getBtcValue())){
			if(Global.MOVE_STOP) {
				//Update for a new value (Move Stop) - Only Sell if the price get the PERCENTUAL_LOSE
				shopping.setBtcValue(Double.parseDouble(marketSummary.result.get(0).Ask));
				shoppingRepository.save(shopping);
				System.out.println("<ShoppingLogic> Profit and Move stop. New value is "+valueForSell);
				updateFinalResult(true);
			}else {
				//Sell ​​at a profit 
				System.out.println("<ShoppingLogic> Sell coin: "+shopping.getCoin() +" whit a profit");
				sellCoin(true, shopping, marketSummary);
			}
		}else if(valueForSell <= calcLoseValue(shopping.getQuantity(), shopping.getBtcValue())){
			//Sell ​​at a loss
			System.out.println("<ShoppingLogic> Sell coin: "+shopping.getCoin() +" whit a loss");
			sellCoin(false, shopping, marketSummary);
		}else{
			System.out.println("<ShoppingLogic> Waiting for a value to sell. "
					+ "Value buyed for "+shopping.getQuantity()*shopping.getFirstBtcValue()+", "
							+ "Actual value is "+valueForSell);
		}
	}
	
	private void sellCoin(Boolean isProfit, Shopping shopping, MarketSummary marketSummary){
		
		BuySellLimit sellConfirmed = 
				!Global.SIMULATION ? client.sellLimit(shopping.getCoin(), shopping.getQuantity(), Double.parseDouble(marketSummary.result.get(0).Ask))
						    : new BuySellLimit(true);
				
		if(sellConfirmed.success){
			System.out.println("<ShoppingLogic> -- Sell Order confirmed, Currency: "+shopping.getCoin().replaceAll("BTC-", "")
					+" Value: "+(shopping.getQuantity()*Double.parseDouble(marketSummary.result.get(0).Ask)));
			
			shopping.setOrderUuid(sellConfirmed.result.uuid);
			shopping.setSellProfit(isProfit);
			shopping.setSold(true);
			shoppingRepository.save(shopping);
		}else{
			throw new RuntimeException("<ShoppingLogic> -- Problems with Sell Order: "+sellConfirmed.message);
		}
		if(!Global.stopProcess()){
			updateFinalResult(isProfit);
		}
	}
	
	public void updateFinalResult(Boolean isProfit){
		Result result = resultRepository.findOne(1L);
		if(result == null) result = new Result(1L, 0L, 0L);
		
		if(isProfit){
			result.setSellProfit(result.getSellProfit()+1);
		}else{
			result.setSellLoss(result.getSellLoss()+1);
		}
		resultRepository.save(result);
	}
	
	private Double quantityForBuy(Double coinValue){
		return Global.BTC_QUANTITY_BUY_PER_COIN/coinValue;
	}
	
	private Double calcProfitValue(Double quantity, Double coinValueShopping){
		return (quantity*coinValueShopping)+(((quantity*coinValueShopping)*Global.PERCENTUAL_PROFIT)/100);
	}
	
	private Double calcLoseValue(Double quantity, Double coinValueShopping){
		return (quantity*coinValueShopping)-(((quantity*coinValueShopping)*Global.PERCENTUAL_LOSE)/100);
	}
}
