package br.com.bittrexbot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.bittrexbot.logic.v1.CoinLogics;

public class Global {
	
	//API KEY
	public static String KEY = "9ab327b660e646108bc072d8533aeacf";
	public static String SECRET = "ZZ6SIACVFZCWJ6BY";
	
	//Main Logic
	public static Boolean SIMULATION = true;
	public static int AMOUNT_COINS_TO_WORK = 10;
	public static int STOP_HOUR = 30;
	public static Double MINIMUN_BASE_VOLUME_TO_BUY = 10.0;
	
	//Purchase Logics to be used
	public static CoinLogics[] logics = {CoinLogics.LogicLocalHistory};
	public static br.com.bittrexbot.logic.v3.CoinLogics[] logicsv3 = {br.com.bittrexbot.logic.v3.CoinLogics.LogicLocalHistory};
	
	//Shopping Logic
	public static Double BTC_QUANTITY_BUY_PER_COIN = 0.001;
	public static Double PERCENTUAL_PROFIT = 3.0;
	public static Double PERCENTUAL_LOSE = 3.0;
	public static Boolean MOVE_STOP = true;
	
	//Local History Logic
	public static int LOCAL_HISTORY_LOGIC_MINUTES = 2;
	public static int LOCAL_HISTORY_LOGIC_PERCENTUAL = 2;
		
	public static Boolean stopProcess(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
		if(Integer.parseInt(dateFormat.format(new Date())) >= STOP_HOUR) return true;
		return false;
	}
}
