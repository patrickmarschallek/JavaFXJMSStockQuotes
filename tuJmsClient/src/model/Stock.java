package model;

import java.io.Serializable;
import java.util.Hashtable;

public class Stock implements Serializable {

	private static final long serialVersionUID = 9047270345084518024L;

	private String wknr;
	private String isin;
	private String name;
	private Hashtable<Long, Double> quotes;

	public Stock(String name) {
		super();
		this.name = name;
	}

	public Stock(String wknr, String isin, String name) {
		this(name);
		this.wknr = wknr;
		this.isin = isin;
	}

	public String getWknr() {
		return wknr;
	}

	public String getIsin() {
		return isin;
	}

	public String getName() {
		return name;
	}

	public Hashtable<Long, Double> getQuotes() {
		return quotes;
	}

	public void addQuote(long timestamp, double quote) {
		this.quotes.put(timestamp, quote);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isin == null) ? 0 : isin.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((wknr == null) ? 0 : wknr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stock other = (Stock) obj;
		if (isin == null) {
			if (other.isin != null)
				return false;
		} else if (!isin.equals(other.isin))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (wknr == null) {
			if (other.wknr != null)
				return false;
		} else if (!wknr.equals(other.wknr))
			return false;
		return true;
	}
}
