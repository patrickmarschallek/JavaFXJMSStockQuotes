package ui.handler;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.XYChart.Series;
import model.StockQuote;
import ui.components.Dialog;
import ui.main.MainFrame;

public class UnsubscribeHandler implements EventHandler<ActionEvent> {

	private MainFrame frame;

	public UnsubscribeHandler(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	public void handle(ActionEvent arg0) {
		String stock = this.frame.getTxtUnsubscribe().getText();

		this.removeStock(stock, this.frame.getLineChart().getData(), this.frame
				.getTable().getItems());
		this.frame.getTxtUnsubscribe().setText("");
	}

	private int findSerieIndex(String stockName,
			final ObservableList<Series<String, Number>> chartSeries) {
		int index = -1;
		for (Series<String, Number> stockQuote : chartSeries) {
			if (stockName.equals(stockQuote.getName())) {
				index = this.frame.getLineChart().getData().indexOf(stockQuote);
			}
		}
		return index;
	}

	private int findTableItemIndex(String stockName,
			final ObservableList<StockQuote> tableList) {
		int index = -1;
		for (StockQuote stockQuote : tableList) {
			if (stockName.equals(stockQuote.getIsin())
					|| stockName.equals(stockQuote.getWkn())
					|| stockName.equals(stockQuote.getName())) {
				index = this.frame.getTable().getItems().indexOf(stockQuote);
			}
		}
		return index;
	}

	private void removeStock(String stockName,
			final ObservableList<Series<String, Number>> chartSeries,
			final ObservableList<StockQuote> tableList) {
		final int serieIndex = this.findSerieIndex(stockName, chartSeries);
		final int itemIndex = this.findTableItemIndex(stockName, tableList);
		if (serieIndex != -1 && itemIndex != -1) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					tableList.remove(itemIndex);
					chartSeries.remove(serieIndex);
				}
			});
		} else {
			Dialog.showErrorDialog(this.frame.getPrimaryStage(),
					"this stock isn't in both lists",
					"Non existing topic", "Warning");;
		}
	}

}
