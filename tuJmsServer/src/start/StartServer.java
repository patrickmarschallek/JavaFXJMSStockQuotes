package start;

import util.StockExchange;

public class StartServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StockExchange exchangeServer = new StockExchange();
		exchangeServer.run();
	}

}
