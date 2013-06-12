package model;

import java.io.Serializable;
import java.sql.Time;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class StockQuote implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8029039736185979373L;

	private final SimpleStringProperty isin;
	private final SimpleStringProperty wkn;
	private final SimpleStringProperty name;
	private final SimpleDoubleProperty quote;
	private final SimpleLongProperty timeInMillis;

	public StockQuote(String isin, String wkn, String name, double quote,
			long timeInMillis) {
		this.isin = new SimpleStringProperty(isin);
		this.wkn = new SimpleStringProperty(wkn);
		this.name = new SimpleStringProperty(name);
		this.quote = new SimpleDoubleProperty(quote);
		this.timeInMillis = new SimpleLongProperty(timeInMillis);
	}

	public String getIsin() {
		return isin.get();
	}

	public String getName() {
		return name.get();
	}

	public String getWkn() {
		return wkn.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getQuote() {
		return String.valueOf(quote.get());
	}

	public double getQuotePlain() {
		return quote.get();
	}

	public void setQuote(double quote) {
		this.quote.set(quote);
	}

	public long getTimeInMillisPlain() {
		return this.timeInMillis.get();
	}

	public String getTimeInMillis() {
		Time time = new Time(this.timeInMillis.get());
		return time.toString();
	}

	public void setTimeInMillis(long timeInMillis) {
		this.timeInMillis.set(timeInMillis);
	}

	public void setWkn(String wkn2) {
		this.wkn.set(wkn2);
	}

	public void setIsin(String isin) {
		this.isin.set(isin);
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof StockQuote) {
			StockQuote quote = (StockQuote) arg0;
			if (quote.getName().equals(this.getName())
					|| quote.getWkn().equals(this.getWkn())
					|| quote.getIsin().equals(this.getIsin())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
