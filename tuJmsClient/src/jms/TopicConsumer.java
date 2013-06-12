package jms;

import java.io.Serializable;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.Topic;

import jms.handle.MessageHandler;

import org.apache.activemq.ActiveMQConnectionFactory;

import ui.main.MainFrame;

public class TopicConsumer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient ActiveMQConnectionFactory factory;
	private transient Connection connection;
	private transient Session session;
	private transient MessageListener messageHanlder;
	private transient Queue requestsQueue;
	
	private String url;
	private Topic topic;

	public TopicConsumer(String url, MainFrame frame) throws JMSException {
		if (url == null) {
			this.url = "tcp://localhost:61616";
		} else {
			this.url = url;
		}
		this.messageHanlder = new MessageHandler(frame);
		init();

	}

	private void init() throws JMSException {
		this.factory = new ActiveMQConnectionFactory(this.url);
		this.connection = this.factory.createConnection();
		this.session = this.connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		requestsQueue = session.createQueue("requests");
	}

	public void requestReply(String stockName) throws JMSException {

		TemporaryQueue replyQueue = session.createTemporaryQueue();
		MessageConsumer qConsumer = session.createConsumer(replyQueue);
		qConsumer.setMessageListener(this.messageHanlder);

		Message message = session.createMessage();
		message.setJMSReplyTo(replyQueue);
		message.setStringProperty("stockName", stockName);
		String correlationId = this.createRandomString();
		message.setJMSCorrelationID(correlationId);

		session.createProducer(requestsQueue).send(
				message);
	}
		
	public void subscribe(String stockName) throws JMSException {
		this.topic = session.createTopic("dax." + stockName);
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(this.messageHanlder);
		this.connection.start();
	}

	public void unsubscribe() throws JMSException {
		this.connection.close();
	}

	public Connection getConnection() {
		return this.connection;
	}

	private String createRandomString() {
		Random random = new Random(System.currentTimeMillis());
		long randomLong = random.nextLong();
		return Long.toHexString(randomLong);
	}
	
}
