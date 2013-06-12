package ui.handler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ui.main.MainFrame;
import util.Serializer;

public class CloseHandler implements EventHandler<ActionEvent> {

	private MainFrame frame;

	public CloseHandler(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	public void handle(ActionEvent arg0) {
		Serializer serializer = new Serializer(this.frame.getStocks(),
				"stockList.ser");
		serializer.writeObject();
		this.frame.dispose();
	}

}
