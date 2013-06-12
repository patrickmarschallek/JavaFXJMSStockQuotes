package jms.handle;

import javafx.scene.chart.XYChart.Series;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import model.StockQuote;
import ui.main.MainFrame;

public class MessageHandler implements MessageListener {

	private MainFrame frame;

	public MessageHandler(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	public void onMessage(Message arg0) {
		ObjectMessage message = (ObjectMessage) arg0;
		StockQuote quote;
		util.StockQuote messageQuote;
		try {
			messageQuote = (util.StockQuote) message.getObject();
			quote = new StockQuote(messageQuote.getIsin(),
					messageQuote.getWkn(), messageQuote.getName(),
					messageQuote.getQuote(), messageQuote.getTimeInMillis());

			for (String stockName : this.frame.getSerieMap().keySet()) {
				if (quote.getName().equals(stockName)) {
					Series<String, Number> serie = this.frame.getSerieMap()
							.get(stockName);
					this.frame.fillSerie(serie, quote.getTimeInMillisPlain(),
							quote.getQuotePlain());
				}
			}

			this.frame.getTable().updateTableObjects(quote);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
