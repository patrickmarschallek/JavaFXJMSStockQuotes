package ui.handler;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ui.main.MainFrame;
import util.Serializer;
import util.StockQuote;

public class CloseHandler implements EventHandler<ActionEvent> {

	private MainFrame frame;

	public CloseHandler(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	public void handle(ActionEvent arg0) {
		ArrayList<StockQuote> list = new ArrayList<StockQuote>();
				
		for(model.StockQuote stockQuote:this.frame.getTable().getItems()){
			list.add(new StockQuote(stockQuote.getWkn(), stockQuote.getIsin(), stockQuote.getName(), stockQuote.getQuotePlain(), stockQuote.getTimeInMillisPlain()));
		}
		
		Serializer serializer = new Serializer(list,
				"stockList.ser");
		serializer.writeObject();
		this.frame.dispose();
	}

}
