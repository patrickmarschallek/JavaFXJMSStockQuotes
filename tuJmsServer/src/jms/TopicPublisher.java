package jms;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import util.StockQuote;

public class TopicPublisher {
	
    private Connection connection;
    private Session session;
    private MessageProducer publisher;
    private String url;
    private Topic topic;
    
    public TopicPublisher(String url){
        if(url == null){
        	this.url = "tcp://localhost:61616";
        }else{
        	this.url = url;
        }
    	init();
    }
    
    public void init(){
    	ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
    	
        try {
			connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
    
    public void publishObjectMessage(StockQuote quote){
        try {
        	topic = session.createTopic("dax."+quote.getName());
			publisher = session.createProducer(topic);
	        publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			publisher.send(session.createObjectMessage(quote));
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
    
    public void close(){
        try {
			connection.stop();
	        connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
}
