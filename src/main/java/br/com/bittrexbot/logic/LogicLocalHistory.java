package br.com.bittrexbot.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.bittrexbot.model.LocalHistory;
import br.com.bittrexbot.model.Shopping;
import br.com.bittrexbot.repository.LocalHistoryRepository;
import br.com.bittrexbot.repository.ShoppingRepository;
import br.com.bittrexbot.rest.client.BittrexClient;
import br.com.bittrexbot.rest.model.MarketSummary;
import br.com.bittrexbot.utils.Global;

/**
 * @author Osmar
 * This logic is based on the currency appreciation percentage in a x-minute interval
 */
@Component
public class LogicLocalHistory {

	private List<String> disregardCoins = new ArrayList<String>();
	
	@Autowired
	private ShoppingRepository shoppingRepository;
	@Autowired
	private LocalHistoryRepository localHistoryRepository;
	
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
		
		//Check date parameter
		Date dateFind = new Date(Calendar.getInstance().getTimeInMillis() - (Global.LOCAL_HISTORY_LOGIC_MINUTES *60000));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String dateFindString = sdf.format(dateFind);
		
		System.out.println("<LogicLocalHistory> -- Saving data / Checking date: "+dateFindString);
		
		MarketSummary saveSummaries = client.getMarketSummaries();
		saveSummaries.result.stream()
			.filter(result -> Double.parseDouble(result.BaseVolume) >= Global.MINIMUN_BASE_VOLUME_TO_BUY)
			.filter(result -> result.MarketName.startsWith("BTC-") && !disregardCoins.contains(result.MarketName))
			.filter(result -> testCoins != null ? (testCoins.stream().filter(s -> s.testExistentMarket(result.MarketName)).count() > 0) : true)
			.forEach(result -> {
			
			//Saves the current value of all currencies
			LocalHistory localHistorySave = new LocalHistory(result.MarketName, Double.parseDouble(result.Last));
			localHistoryRepository.save(localHistorySave);
			
			if(eligibleCoins.size() < quantityOfCoins) {
				//Check if the local base already has enough data for the parameterized time
				LocalHistory localHistory = localHistoryRepository.findByDate(result.MarketName, dateFindString);
				if(localHistory != null){
					//Check percent
					Double percentValue = (Double.parseDouble(result.Last) * 100) / localHistory.getLastValue();
					if(percentValue >= 100+Global.LOCAL_HISTORY_LOGIC_PERCENTUAL){
						System.out.println("<LogicLocalHistory> -- Currency "+result.MarketName+" grew "+(percentValue-100)+"%");
						eligibleCoins.add(client.getMarketSummary(result.MarketName));
					}
				}
			}
			
		});
		return eligibleCoins;
	}

}
