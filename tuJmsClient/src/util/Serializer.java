package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.jms.JMSException;

import jms.TopicConsumer;
import model.Stock;
import ui.main.MainFrame;

public class Serializer {

	private Object objToSerialize;
	private String fileName;

	public Serializer(Object obj, String fileName) {
		this.objToSerialize = obj;
		this.fileName = fileName;
	}

	public void writeObject() {
		OutputStream file;
		try {
			file = new FileOutputStream(this.fileName);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(this.objToSerialize);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<StockQuote> readObject(MainFrame frame) throws IOException {
		ArrayList<StockQuote> recoveredStocks = null;
		// use buffering
		InputStream file;
		try {
			file = new FileInputStream(this.fileName);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			// deserialize the List
			recoveredStocks = (ArrayList<StockQuote>) input.readObject();
			file.close();
			buffer.close();
			input.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return this.reinitializeConsumer(recoveredStocks, frame);

	}

	private ArrayList<StockQuote> reinitializeConsumer(
			ArrayList<StockQuote> list, final MainFrame frame) {
		for (StockQuote stockQuote : list) {
			try {
				TopicConsumer consumer = new TopicConsumer(null, frame);
				consumer.subscribe(stockQuote.getName());
				consumer.requestReply(stockQuote.getName());
				Stock stock = new Stock(stockQuote.getName());
				model.StockQuote tableQuote = new model.StockQuote(
						stockQuote.getIsin(), stockQuote.getWkn(),
						stockQuote.getName(), stockQuote.getQuote(),
						stockQuote.getTimeInMillis());
				frame.getTable().addQuote(tableQuote);
				frame.createSerie(stock);
				frame.getStocks().add(stock);
			} catch (JMSException e) {
				e.printStackTrace();
			}

		}
		return list;
	}

	public Object getObjToSerialize() {
		return objToSerialize;
	}

	public void setObjToSerialize(Object objToSerialize) {
		this.objToSerialize = objToSerialize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
