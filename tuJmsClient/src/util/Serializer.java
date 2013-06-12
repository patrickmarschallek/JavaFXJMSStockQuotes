package util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;

import model.StockQuote;

import jms.TopicConsumer;

public class Serializer {
	
	
	private Object objToSerialize;
	private String fileName;
	
	public Serializer(Object obj, String fileName){
		this.objToSerialize = obj;
		this.fileName = fileName;
	}

	public void writeObject(){
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
	
	public Map<String,Map<StockQuote, TopicConsumer>> readObject(){
		
		return null;
		
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
