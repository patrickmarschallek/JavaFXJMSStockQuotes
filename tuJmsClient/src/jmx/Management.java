package jmx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class Management {

	private static final String JMS_DOMAIN = "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Topic,destinationName=*";
	private static final String JMX_CONNECTION_URL = "service:jmx:rmi:///jndi/rmi://:1099/jmxrmi";
	private static final String REGEX = ".*ActiveMQ.*|.*Advisory.*";
	private static final String DESTINATION_TYPE = "destinationType";
	private static final String DESTINATIONNAME = "destinationName";

	public List<String> getTopics() {
		List<String> topicList = new ArrayList<String>();
		List<String> jmxList = getJmxObjectList();

		for (String objname : jmxList) {
			if (!objname.matches(REGEX)) {
				topicList.add(objname);
			}

		}
		return topicList;
	}

	private List<String> getJmxObjectList() {
		JMXServiceURL url;
		List<String> list = new ArrayList<String>();
		try {
			url = new JMXServiceURL(JMX_CONNECTION_URL);
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
			ObjectName objName = new ObjectName(JMS_DOMAIN);
			Set<ObjectName> objList = new TreeSet<ObjectName>(mbsc.queryNames(
					objName, null));
			for (ObjectName objname : objList) {
				if (objname.getKeyProperty(DESTINATION_TYPE).equals("Topic")) {
					list.add(objname.getKeyProperty(DESTINATIONNAME));
				}

			}
			jmxc.close();
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

		return list;
	}
}
