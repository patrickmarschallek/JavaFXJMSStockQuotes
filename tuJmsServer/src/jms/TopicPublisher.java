package jms;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import util.StockExchange;
import util.StockQuote;

public class TopicPublisher {

	private Connection connection;
	private Session session;
	private MessageProducer publisher;
	private String url;
	private Topic topic;
	private ActiveMQConnectionFactory factory;
	private Queue requestsQueue;
	private MessageConsumer requestConsumer;

	public TopicPublisher(String url) {
		if (url == null) {
			this.url = "tcp://localhost:61616";
		} else {
			this.url = url;
		}
		init();
	}

	public void init() {
		this.factory = new ActiveMQConnectionFactory(this.url);

		try {
			connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			requestsQueue = session.createQueue("requests");
			requestConsumer = session.createConsumer(requestsQueue);
			requestConsumer.setMessageListener(new MessageListener() {
				public void onMessage(Message arg0) {
					handleRequestMessage(arg0);
				}
			});
			connection.start();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void handleRequestMessage(Message msg) {
		try {
			String stockName = msg.getStringProperty("stockName");
			Destination replyDest = msg.getJMSReplyTo();

			QueueConnection qConnection = factory.createQueueConnection();
			QueueSession qSession = qConnection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer mp = qSession.createProducer(replyDest);
			mp.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			mp.send(qSession.createObjectMessage(StockExchange
					.getCurrentQuote(stockName)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void publishObjectMessage(StockQuote quote) {
		try {
			topic = session.createTopic(quote.getName());
			publisher = session.createProducer(topic);
			publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			publisher.send(session.createObjectMessage(quote));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			connection.stop();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
