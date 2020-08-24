package br.com.bittrexbot.logic.v3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.bittrexbot.model.Result;
import br.com.bittrexbot.model.Shopping;
import br.com.bittrexbot.repository.ResultRepository;
import br.com.bittrexbot.repository.ShoppingRepository;
import br.com.bittrexbot.rest.client.BittrexClientV3;
import br.com.bittrexbot.rest.model.v3.MarketSummary;
import br.com.bittrexbot.rest.model.v3.MarketTicker;
import br.com.bittrexbot.rest.model.v3.Order.Direction;
import br.com.bittrexbot.rest.model.v3.OrderResponse;
import br.com.bittrexbot.utils.Global;

/**
 * @author Osmar
 * This class is responsible to buy and sell
 *
 */
@Component
public class ShoppingLogicV3 {
	
	@Autowired
	private BittrexClientV3 client;
	
	@Autowired
	private ShoppingRepository shoppingRepository;
	@Autowired
	private ResultRepository resultRepository;

	/**
	 * Buy Method
	 * @param marketSummaryCoin
	 */
	public void buy(MarketSummary marketSummaryCoin) {
		MarketTicker ticker = client.getMarketTicker(marketSummaryCoin.getSymbol());
		Double quantity = quantityForBuy(ticker.getAskRate());
		System.out.println("<ShoppingLogic> -- Buying "+quantity+" "+marketSummaryCoin.getSymbol()+" Price: "+ticker.getAskRate());
		OrderResponse orderResponse = 
				!Global.SIMULATION ? client.order(marketSummaryCoin.getSymbol(), Direction.BUY, quantity) : new OrderResponse("SIMULATION_ID");
		 
		System.out.println("<ShoppingLogic> -- Buy Order confirmed");
		shoppingRepository.save(
				new Shopping(marketSummaryCoin.getSymbol(), quantity,
						ticker.getBidRate(),
						ticker.getBidRate(), orderResponse.getId()));

	}
	
	/**
	 * Sell Method
	 * @param marketSummary
	 * @param shopping
	 * @param simulation
	 */
	public void sell(MarketSummary marketSummary, Shopping shopping) {
		MarketTicker ticker = client.getMarketTicker(marketSummary.getSymbol());
		Double valueForSell = ticker.getBidRate() * shopping.getQuantity();
		
		System.out.println("<ShoppingLogic> Checking coin: "+shopping.getCoin()+" Ask: "+ticker.getBidRate()+" Buyed at: "+shopping.getBtcValue());
		
		if(Global.stopProcess()){
			System.out.println("<ShoppingLogic> Sell coin: "+shopping.getCoin() +" --- STOP PROCESS");
			sellCoin(false, shopping, marketSummary, ticker);
		}else if(valueForSell >= calcProfitValue(shopping.getQuantity(), shopping.getBtcValue())){
			if(Global.MOVE_STOP) {
				//Update for a new value (Move Stop) - Only Sell if the price get the PERCENTUAL_LOSE
				shopping.setBtcValue(ticker.getBidRate());
				shoppingRepository.save(shopping);
				System.out.println("<ShoppingLogic> Profit and Move stop. New value is "+valueForSell);
				updateFinalResult(true);
			}else {
				//Sell ​​at a profit 
				System.out.println("<ShoppingLogic> Sell coin: "+shopping.getCoin() +" whit a profit");
				sellCoin(true, shopping, marketSummary, ticker);
			}
		}else if(valueForSell <= calcLoseValue(shopping.getQuantity(), shopping.getBtcValue())){
			//Sell ​​at a loss
			System.out.println("<ShoppingLogic> Sell coin: "+shopping.getCoin() +" whit a loss");
			sellCoin(false, shopping, marketSummary, ticker);
		}else{
			System.out.println("<ShoppingLogic> Waiting for a value to sell. "
					+ "Value buyed for "+shopping.getQuantity()*shopping.getFirstBtcValue()+", "
							+ "Actual value is "+valueForSell);
		}
	}
	
	private void sellCoin(Boolean isProfit, Shopping shopping, MarketSummary marketSummary, MarketTicker ticker){
		
		OrderResponse orderResponse = 
				!Global.SIMULATION ? client.order(shopping.getCoin(), Direction.SELL, shopping.getQuantity()) : new OrderResponse("SIMULATION_ID");
				
		System.out.println("<ShoppingLogic> -- Sell Order confirmed, Currency: "+shopping.getCoin().replaceAll("BTC-", "")
				+" Value: "+(shopping.getQuantity()*ticker.getBidRate()));
		
		shopping.setOrderUuid(orderResponse.getId());
		shopping.setSellProfit(isProfit);
		shopping.setSold(true);
		shoppingRepository.save(shopping);

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
