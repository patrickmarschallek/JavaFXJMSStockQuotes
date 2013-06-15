package ui.components;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import jmx.Management;
import ui.handler.CheckBoxHandler;
import ui.main.MainFrame;
import util.StockQuote;

public class ListPanel extends ListView<CheckBox> {

	public ObservableList<CheckBox> data = FXCollections.observableArrayList();
	private MainFrame frame;

	public ListPanel(final MainFrame frame, ArrayList<StockQuote> list) {
		super();
		this.frame = frame;
		this.init(list);
	}

	private void init(ArrayList<StockQuote> reinitializedList) {
		this.setPrefSize(200, 250);
		this.setEditable(true);
		Management mx = new Management();
		List<String> availablestock = mx.getTopics();
		for (String stockName : availablestock) {
			CheckBox cb = new CheckBox(stockName);
			cb.setId(stockName);
			this.data.add(cb);
			for (StockQuote quote : reinitializedList) {
				if (quote.getName().equals(stockName)) {
					cb.setSelected(true);
				}
			}
			cb.selectedProperty().addListener(new CheckBoxHandler(this, cb));
		}
		this.setItems(this.data);
	}

	public MainFrame getFrame() {
		return this.frame;
	}

}
