package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import jms.TopicPublisher;

public class StockExchange {

	private static TopicPublisher publisher;
	private final static ArrayList<StockQuote> stockList = generateQuotes();

	public StockExchange() {
		StockExchange.publisher = new TopicPublisher(null);
	}

	public void run() {
		for (StockQuote stock : StockExchange.stockList) {
			Timer timer = new Timer();
			ExchangeTask exTask = new ExchangeTask(stock);
			timer.scheduleAtFixedRate(exTask,
					new Date(System.currentTimeMillis()),
					StockExchange.random(5, 15));
		}
	}

	public void close() {
		publisher.close();
	}

	private static ArrayList<StockQuote> generateQuotes() {
		ArrayList<StockQuote> quoteList = new ArrayList<StockQuote>();

		quoteList.add(new StockQuote("555750", "DE0005557508",
				"Deutsche Telekom Aktie", 8.76, System.currentTimeMillis()));
		quoteList.add(new StockQuote("766400", "DE0007664005",
				"Volkswagen St Aktie", 152.63, System.currentTimeMillis()));
		quoteList.add(new StockQuote("710000", "DE0007100000", "Daimler Aktie",
				20.02, System.currentTimeMillis()));
		quoteList.add(new StockQuote("870747", "US5949181045",
				"Microsoft Aktie", 25.96, System.currentTimeMillis()));
		quoteList.add(new StockQuote("A1EWWW", "DE00A1EWWW0", "Adidas Aktie",
				81.66, System.currentTimeMillis()));
		quoteList.add(new StockQuote("BASF11", "DE000BASF111", "BASF Aktie",
				72.30, System.currentTimeMillis()));
		quoteList.add(new StockQuote("ENAG99", "DE000ENAG999", "E.ON SE Aktie",
				13.10, System.currentTimeMillis()));
		return quoteList;
	}

	private static class ExchangeTask extends TimerTask {

		private StockQuote stockQuote;

		public ExchangeTask(StockQuote stockQuote) {
			this.stockQuote = stockQuote;
		}

		@Override
		public void run() {
			double newStockQuote = 0;
			newStockQuote = stockQuote.getQuote() + this.computeVariance(2);
			stockQuote.setQuote(newStockQuote);
			stockQuote.setTimeInMillis(System.currentTimeMillis());
			publisher.publishObjectMessage(stockQuote);
		}

		private long computeVariance(double variance) {
			Double randomValue = Math.random() * 1000;
			int randomSign = (randomValue.intValue() % 2 == 0) ? -1 : 1;
			long value = (long) (0 + (Math.random() * ((variance - 0) + 1)));
			return value * randomSign;
		}
	}

	public static StockQuote getCurrentQuote(String stockName) {
		for (StockQuote quote : stockList) {
			if (quote.getName().equals(stockName)) {
				return quote;
			}
		}
		return null;
	}

	public static long random(double min, double max) {
		return (long) (1000 * (min + (Math.random() * ((max - min) + 1))));
	}

}
