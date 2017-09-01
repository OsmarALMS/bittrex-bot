package br.com.bittrexbot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.bittrexbot.logic.CoinLogics;

public class Global {
	
	//API KEY
	public static String KEY = "";
	public static String SECRET = "";
	
	//Main Logic
	public static Boolean SIMULATION = true;
	public static int AMOUNT_COINS_TO_WORK = 4;
	public static int STOP_HOUR = 20;
	public static Double MINIMUN_BASE_VOLUME_TO_BUY = 100.0;
	
	//Purchase Logics to be used
	public static CoinLogics[] logics = {CoinLogics.LogicLocalHistory};
	
	//Shopping Logic
	public static Double BTC_QUANTITY_BUY_PER_COIN = 0.01;
	public static Double PERCENTUAL_PROFIT = 4.0;
	public static Double PERCENTUAL_LOSE = 2.0;
	
	//Local History Logic
	public static int LOCAL_HISTORY_LOGIC_MINUTES = 10;
	public static int LOCAL_HISTORY_LOGIC_PERCENTUAL = 5;
	
	public static Boolean stopProcess(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
		if(Integer.parseInt(dateFormat.format(new Date())) >= STOP_HOUR) return true;
		return false;
	}
}
