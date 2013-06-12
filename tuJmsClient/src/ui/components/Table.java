package ui.components;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.StockQuote;
import ui.main.MainFrame;

public class Table extends TableView<StockQuote> {

	private MainFrame frame;
	private final ObservableList<StockQuote> stockList = FXCollections
			.observableArrayList(new ArrayList<StockQuote>());

	public Table(final MainFrame mainFrame) {
		super();
		this.frame = mainFrame;
		init();
	}

	private void init() {

		this.getColumns().add(this.createTableColumn("ISIN", 50, "isin"));
		this.getColumns().add(this.createTableColumn("WKN", 50, "wkn"));
		this.getColumns()
				.add(this.createTableColumn("Stock-Name", 150, "name"));
		this.getColumns().add(this.createTableColumn("Price", 50, "quote"));
		this.getColumns().add(
				this.createTableColumn("Time", 90, "timeInMillis"));

		this.setPrefSize(800, 100);
		
		// bindings
		this.setItems(this.stockList);
	}

	private TableColumn<StockQuote, String> createTableColumn(
			String columnTitle, int minWidth, String propertyName) {
		TableColumn<StockQuote, String> column = new TableColumn<StockQuote, String>(
				columnTitle);
		column.setMinWidth(minWidth);
		column.setCellValueFactory(new PropertyValueFactory<StockQuote, String>(
				propertyName));
		return column;
	}

	public void addQuote(final StockQuote quote) {
		boolean conatains = false;
		for (StockQuote stockQuote : this.stockList) {
			if (stockQuote.getName().equals(quote.getName())) {
				conatains = true;
			}
		}
		if (!conatains) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					stockList.add(quote);
				}
			});
		} else {
			Dialog.showErrorDialog(this.frame.getPrimaryStage(),
					"You are allready subscribed to this topic",
					"Redundant Topic", "Warning");
		}
	}

	public void removeTableObject(String value) {
		for (StockQuote stockQuote : this.stockList) {
			if (stockQuote.getName().equals(value)
					|| stockQuote.getWkn().equals(value)
					|| stockQuote.getIsin().equals(value)) {
				this.stockList.remove(this.stockList.indexOf(stockQuote));
			}
		}
	}

}
