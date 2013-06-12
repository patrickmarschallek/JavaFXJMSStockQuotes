package ui.handler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javax.jms.JMSException;

import jms.TopicConsumer;
import model.Stock;
import ui.components.Dialog;
import ui.main.MainFrame;

public class SearchSubmit implements EventHandler<ActionEvent> {

	private MainFrame frame;

	public SearchSubmit(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	public void handle(ActionEvent arg0) {
		try {
			TopicConsumer consumer = new TopicConsumer(null, this.frame);
			consumer.subscribe(this.getStockName());
			Stock stock = new Stock(this.getStockName());

			this.addStock(stock);
			this.frame.createSerie(stock);
			
		} catch (JMSException e) {
			Dialog.showErrorDialog(this.frame.getPrimaryStage(),
					"Something went wrong: \n" + e.getMessage(),
					"Error Dialog", "Error");

		}

		this.clearStockName();
	}

	private String getStockName() {
		return this.frame.getTxtSearchField().getText();
	}

	private void clearStockName() {
		this.frame.getTxtSearchField().setText("");
	}

	private void addStock(Stock stock) {
		this.frame.getStocks().add(stock);
	}

}
