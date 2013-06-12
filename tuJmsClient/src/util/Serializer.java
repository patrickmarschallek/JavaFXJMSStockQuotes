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
import java.util.Map;

import javax.jms.JMSException;

import jms.TopicConsumer;
import model.Stock;
import model.StockQuote;
import ui.components.Table;
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

	public ArrayList<StockQuote> readObject(MainFrame frame)
			throws IOException {
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
			ArrayList<StockQuote> list,final MainFrame frame) {
		for (StockQuote stockQuote : list) {
			try {
				TopicConsumer consumer = new TopicConsumer(null, frame);
				consumer.subscribe(stockQuote.getName());
				Stock stock = new Stock(stockQuote.getName());
				frame.getTable().addQuote(stockQuote);
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

	//
	// package util;
	//
	// import java.io.BufferedInputStream;
	// import java.io.BufferedOutputStream;
	// import java.io.FileInputStream;
	// import java.io.FileNotFoundException;
	// import java.io.FileOutputStream;
	// import java.io.IOException;
	// import java.io.InputStream;
	// import java.io.ObjectInput;
	// import java.io.ObjectInputStream;
	// import java.io.ObjectOutput;
	// import java.io.ObjectOutputStream;
	// import java.io.OutputStream;
	// import java.util.HashMap;
	// import java.util.Map;
	//
	// import javax.jms.JMSException;
	//
	// import ui.components.TablePanel;
	//
	// import jms.TopicConsumer;
	//
	// public class Serializer {
	//
	//
	// private Object objToSerialize;
	// private String fileName;
	//
	// public Serializer(Object obj, String fileName){
	// this.objToSerialize = obj;
	// this.fileName = fileName;
	// }
	//
	// public Serializer(String fileName){
	// this.objToSerialize = null;
	// this.fileName = fileName;
	// }
	//
	// public void writeObject(){
	//
	// OutputStream file;
	// try {
	// file = new FileOutputStream(this.fileName);
	// OutputStream buffer = new BufferedOutputStream(file);
	// ObjectOutput output = new ObjectOutputStream(buffer);
	// output.writeObject(this.objToSerialize);
	// output.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public Map<String,Map<StockQuote, TopicConsumer>> readObject(TablePanel
	// tablePanel) throws IOException{
	// Map<String,Map<StockQuote, TopicConsumer>> recoveredStocks = null;
	// //use buffering
	// InputStream file;
	// try {
	// file = new FileInputStream(this.fileName);
	// InputStream buffer = new BufferedInputStream(file);
	// ObjectInput input = new ObjectInputStream (buffer);
	//
	// //deserialize the List
	// recoveredStocks = (Map<String,Map<StockQuote,
	// TopicConsumer>>)input.readObject();
	// file.close();
	// buffer.close();
	// input.close();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// return reinitializeConsumer(recoveredStocks, tablePanel);
	//
	// }
	//
	//
	// private Map<String,Map<StockQuote, TopicConsumer>>
	// reinitializeConsumer(Map<String,Map<StockQuote, TopicConsumer>> list,
	// TablePanel tablePanel){
	// for(Map<StockQuote, TopicConsumer> innerMap:list.values()){
	// for(Map.Entry<StockQuote, TopicConsumer> it:innerMap.entrySet()){
	// StockQuote sQuote = it.getKey();
	// TopicConsumer cons = it.getValue();
	//
	// try {
	// cons.init();
	// cons.subscribe(sQuote.getName());
	// } catch (JMSException e) {
	// e.printStackTrace();
	// }
	//
	// cons.setTablePanel(tablePanel);
	// it.setValue(cons);
	// }
	// }
	// return list;
	// }
	//
	// public Object getObjToSerialize() {
	// return objToSerialize;
	// }
	//
	// public void setObjToSerialize(Object objToSerialize) {
	// this.objToSerialize = objToSerialize;
	// }
	//
	// public String getFileName() {
	// return fileName;
	// }
	//
	// public void setFileName(String fileName) {
	// this.fileName = fileName;
	// }
	//
	//
	//
	// }

}
