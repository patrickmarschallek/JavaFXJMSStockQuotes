package ui.handler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.XYChart.Series;
import model.StockQuote;
import ui.main.MainFrame;

public class UnsubscribeHandler implements EventHandler<ActionEvent> {

	private MainFrame frame;

	public UnsubscribeHandler(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	public void handle(ActionEvent arg0) {
		String stock = this.frame.getTxtUnsubscribe().getText();
		for (StockQuote stockQuote : this.frame.getTable().getItems()) {
			if (stock.equals(stockQuote.getIsin())
					|| stock.equals(stockQuote.getWkn())
					|| stock.equals(stockQuote.getName())) {
				int index = this.frame.getTable().getItems()
						.indexOf(stockQuote);
				this.frame.getTable().getItems().remove(index);
			}
		}
		for (Series<String, Number> stockQuote : this.frame.getLineChart()
				.getData()) {
			if (stock.equals(stockQuote.getName())) {
				int index = this.frame.getLineChart().getData()
						.indexOf(stockQuote);
				this.frame.getLineChart().getData().remove(index);
			}
		}
		this.frame.getTxtUnsubscribe().setText("");
	}

}
