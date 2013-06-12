package util;

import java.io.Serializable;

public class StockQuote implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8029039736185979373L;

	private String wkn;
	private String isin;
	private String name;
	private double quote;
	private long timeInMillis;

	public StockQuote(String wkn, String isin, String name, double quote,
			long timeInMillis) {
		this.wkn = wkn;
		this.isin = isin;
		this.name = name;
		this.quote = quote;
		this.timeInMillis = timeInMillis;
	}

	public String getIsin() {
		return isin;
	}

	public String getWkn() {
		return wkn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getQuote() {
		return quote;
	}

	public void setQuote(double quote) {
		this.quote = quote;
	}

	public long getTimeInMillis() {
		return timeInMillis;
	}

	public void setTimeInMillis(long timeInMillis) {
		this.timeInMillis = timeInMillis;
	}

	@Override
	public boolean equals(Object arg0) {
		StockQuote quote = (StockQuote) arg0;
		if (quote.getName().equals(this.getName())) {
			return true;
		} else {
			return false;
		}
	}

}
