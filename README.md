# bittrex-bot

This project is in the Alpha version, it is possible to perform simulations and real transactions by the bot.
I am building several Logics that will be responsible for choosing the best currency to buy at the moment and selling it with a minimum profit or loss.

## Running

Basically you need to configure the class **br.com.bittrexbot.utils.Global.Java** and put your preferences, after these settings you can run the project directly by class **br.com.bittrexbot.ApiBittrexBot.java**.

The class **br.com.bittrexbot.logic.LogicBot.java** is a schedule set to run every 60 seconds, this class is responsible for all logic. The time set in the phonebook can be changed

**********************************************************************************************
**Be careful, if you set your Key and Secret API in the bot and parameter it to not perform simulations, you will be making real transactions through Bittrex, I will not be responsible for what may occur. =)**
**********************************************************************************************
