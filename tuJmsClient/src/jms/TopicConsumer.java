package jms;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
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

}
