package model;

import java.util.ArrayList;

public class StockQuoteList{

	private ArrayList<Stock> stocks;
	
	public StockQuoteList() {
		this.stocks = new ArrayList<Stock>();
	}
	
	public void addStock(Stock stock){
		this.stocks.add(stock);
	}
	
	public void removeStock(Stock stock){
		this.stocks.remove(stock);
	}
}
