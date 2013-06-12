package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import jms.TopicPublisher;

public class StockExchange {

	private static TopicPublisher publisher;

	public StockExchange() {
		StockExchange.publisher = new TopicPublisher(null);
	}

	public void run() {
		Timer timer = new Timer();
		ExchangeTask exTask = new ExchangeTask();
		timer.scheduleAtFixedRate(exTask, new Date(System.currentTimeMillis()),
				StockExchange.random(5, 15));
	}

	public void close() {
		publisher.close();
	}

	private static ArrayList<StockQuote> generateQuotes() {
		ArrayList<StockQuote> quoteList = new ArrayList<StockQuote>();

		quoteList.add(new StockQuote("555750", "DE0005557508",
				"Deutsche Telekom Aktie", 50.92, System.currentTimeMillis()));
		quoteList.add(new StockQuote("766400", "DE0007664005",
				"Volkswagen St Aktie", 30.92, System.currentTimeMillis()));
		quoteList.add(new StockQuote("710000", "DE0007100000", "Daimler Aktie",
				20.02, System.currentTimeMillis()));
		quoteList.add(new StockQuote("870747", "US5949181045",
				"Microsoft Aktie", 80.92, System.currentTimeMillis()));
		quoteList.add(new StockQuote("A1EWWW", "DE00A1EWWW0", "Adidas Aktie",
				80.92, System.currentTimeMillis()));
		quoteList.add(new StockQuote("BASF11", "DE000BASF111", "BASF Aktie",
				71.45, System.currentTimeMillis()));
		quoteList.add(new StockQuote("ENAG99", "DE000ENAG999", "E.ON SE Aktie",
				12.70, System.currentTimeMillis()));
		return quoteList;
	}

	private static class ExchangeTask extends TimerTask {

		@Override
		public void run() {
			for (StockQuote quote : generateQuotes()) {
				double newStockQuote = 0;
				newStockQuote = quote.getQuote() + this.computeVariance(2);
				quote.setQuote(newStockQuote);
				publisher.publishObjectMessage(quote);
			}

		}

		private long computeVariance(double variance) {
			int randomSign = (((int) Math.random() * 10) % 2 == 0) ? -1 : 1;
			return (long) (0 + (Math.random() * ((variance - 0) + 1))
					* randomSign);
		}
	}

	public static long random(double min, double max) {
		return (long) (1000 * (min + (Math.random() * ((max - min) + 1))));
	}

}
