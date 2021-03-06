package com.comandante.creeper.bot.command;


import com.comandante.creeper.bot.StockPriceManager;
import com.comandante.creeper.core_game.GameManager;
import org.devotionit.vantage.AlphaVantageClient;

public class BotCommandManager {

    private final GameManager gameManager;
    private final WeatherManager weatherManager;
    private final ChuckNorrisManager chuckNorrisManager;
    private final DictionaryManager dictionaryManager;
    private final OmdbManager omdbManager;
    private final WeatherHistoryManager weatherHistoryManager;
    private final CoindeskManager coindeskManager;
    private final QuoteManager quoteManager;
    private final QuoteProcessor quoteProcessor;
    private final StockPriceManager stockPriceManager;

    public BotCommandManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.weatherManager = new WeatherManager(gameManager.getCreeperConfiguration(), gameManager.getIrcBotService());
        this.chuckNorrisManager = new ChuckNorrisManager(gameManager.getCreeperConfiguration());
        this.dictionaryManager = new DictionaryManager(gameManager.getCreeperConfiguration());
        this.omdbManager = new OmdbManager(gameManager.getCreeperConfiguration().getOmdbApiKey());
        this.weatherHistoryManager = new WeatherHistoryManager(gameManager.getMapDBCreeperStorage());
        this.coindeskManager = new CoindeskManager(new CoindeskClient(gameManager.getObjectMapper()));
        this.quoteManager = new QuoteManager(gameManager.getMapDBCreeperStorage().getIrcQuotes());
        this.quoteProcessor = gameManager.getQuoteProcessor();
        this.stockPriceManager = new StockPriceManager(new AlphaVantageClient(gameManager.getCreeperConfiguration().getAlphaVantageApiKey()));
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public WeatherManager getWeatherManager() {
        return weatherManager;
    }

    public ChuckNorrisManager getChuckNorrisManager() {
        return chuckNorrisManager;
    }

    public DictionaryManager getDictionaryManager() {
        return dictionaryManager;
    }

    public OmdbManager getOmdbManager() {
        return omdbManager;
    }

    public WeatherHistoryManager getWeatherHistoryManager() {
        return weatherHistoryManager;
    }

    public CoindeskManager getCoindeskManager() {
        return coindeskManager;
    }

    public QuoteManager getQuoteManager() {
        return quoteManager;
    }

    public QuoteProcessor getQuoteProcessor() {
        return quoteProcessor;
    }

    public StockPriceManager getAlphaVantageManager() {
        return stockPriceManager;
    }
}
