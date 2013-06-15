package ui.handler;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.CheckBox;

import javax.jms.JMSException;

import jms.TopicConsumer;
import model.Stock;
import model.StockQuote;
import ui.components.Dialog;
import ui.components.ListPanel;
import ui.main.MainFrame;

public class CheckBoxHandler implements ChangeListener<Boolean> {

	private CheckBox sender;
	private ListPanel listPanel;

	public CheckBoxHandler(ListPanel list,CheckBox sender){
		this.sender = sender;
		this.listPanel = list;
	}
	
	@Override
	public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val,
			Boolean new_val) {
		if (new_val) {
			this.subscribe(this.sender.getId());
		} else {
			this.unsubscribe(this.sender.getId());
		}
	}

	private void subscribe(String stockName){
		try {
			TopicConsumer consumer = new TopicConsumer(null, this.getFrame());
			consumer.subscribe(stockName);
			Stock stock = new Stock(stockName);

			this.addStock(stock);
			this.getFrame().getTable().addQuote(new StockQuote("", "", stockName, 0.0, 0));
			this.getFrame().createSerie(stock);
			
		} catch (JMSException e) {
			Dialog.showErrorDialog(this.getFrame().getPrimaryStage(),
					"Something went wrong: \n" + e.getMessage(),
					"Error Dialog", "Error");

		}
	}

	private MainFrame getFrame() {
		return this.listPanel.getFrame();
	}

	private void unsubscribe(String stockName){
		this.removeStock(stockName, this.getFrame().getLineChart().getData(), this.getFrame()
				.getTable().getItems());
	}
	
	private int findTableItemIndex(String stockName,
			final ObservableList<StockQuote> tableList) {
		int index = -1;
		for (StockQuote stockQuote : tableList) {
			if (stockName.equals(stockQuote.getIsin())
					|| stockName.equals(stockQuote.getWkn())
					|| stockName.equals(stockQuote.getName())) {
				index = this.getFrame().getTable().getItems().indexOf(stockQuote);
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
			Dialog.showErrorDialog(this.getFrame().getPrimaryStage(),
					"this stock isn't in both lists",
					"Non existing topic", "Warning");;
		}
	}
	
	private int findSerieIndex(String stockName,
			final ObservableList<Series<String, Number>> chartSeries) {
		int index = -1;
		for (Series<String, Number> stockQuote : chartSeries) {
			if (stockName.equals(stockQuote.getName())) {
				index = this.getFrame().getLineChart().getData().indexOf(stockQuote);
			}
		}
		return index;
	}
	
	private void addStock(Stock stock) {
		this.getFrame().getStocks().add(stock);
	}
}
