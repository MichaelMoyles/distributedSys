import java.text.NumberFormat;

import service.message.ClientMessage;
import service.message.QuotationMessage;
import service.message.OfferMessage;

import service.core.ClientInfo;
import service.core.Quotation;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Topic;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

import java.util.HashMap;
import java.util.Map;

public class Main {

	/**
	 * This is the starting point for the application. Here, we must
	 * get a reference to the Broker Service and then invoke the
	 * getQuotations() method on that service.
	 *
	 * Finally, you should print out all quotations returned
	 * by the service.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://localhost:61616");
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

			Topic topic = session.createTopic("APPLICATIONS");
			Queue queue = session.createQueue("OFFERS");
			MessageProducer producer = session.createProducer(topic);
			MessageConsumer consumer = session.createConsumer(queue);

			connection.start();

			Map<ClientInfo, OfferMessage> store = new HashMap<>();

			for (ClientInfo info : clients) {
				ClientMessage clientMessage = new ClientMessage(2L, info);
				ObjectMessage message = session.createObjectMessage(clientMessage);
				producer.send(message);

				Thread.sleep(10000);

				Message receivedMessage = consumer.receive();
				if (receivedMessage instanceof ObjectMessage) {
					Object serviceMessage = ((ObjectMessage) receivedMessage).getObject();
					if (serviceMessage instanceof OfferMessage) {
						OfferMessage offerMessage = (OfferMessage) serviceMessage;
						store.put(info, offerMessage);
					} else {
						System.out.println("Error in receiving the message");
					}
				} else {
					System.out.println("Error in get the object from the message");
				}
			}

			// Create the broker and run the test data
			for (ClientInfo info : clients) {
				OfferMessage offerMessage = store.get(info);
				if (offerMessage != null) {
					displayProfile(offerMessage.getInfo());

					// Retrieve quotations from the broker and display them...
					for (Quotation quotation : offerMessage.getQuotations()) {
						displayQuotation(quotation);
					}
				} else {
					System.out.println("Error in retrieving the message from storage");
				}
				// Print a couple of lines between each client
				System.out.println("\n");
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Display the client info nicely.
	 *
	 * @param info
	 */
	public static void displayProfile(ClientInfo info) {
		System.out.println("|=================================================================================================================|");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println(
				"| Name: " + String.format("%1$-29s", info.name) +
				" | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
				" | Age: " + String.format("%1$-30s", info.age)+" |");
		System.out.println(
				"| Weight/Height: " + String.format("%1$-20s", info.weight+"kg/"+info.height+"m") +
				" | Smoker: " + String.format("%1$-27s", info.smoker?"YES":"NO") +
				" | Medical Problems: " + String.format("%1$-17s", info.medicalIssues?"YES":"NO")+" |");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Display a quotation nicely - note that the assumption is that the quotation will follow
	 * immediately after the profile (so the top of the quotation box is missing).
	 *
	 * @param quotation
	 */
	public static void displayQuotation(Quotation quotation) {
		System.out.println(
				"| Company: " + String.format("%1$-26s", quotation.company) +
				" | Reference: " + String.format("%1$-24s", quotation.reference) +
				" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Test Data
	 */
	public static final ClientInfo[] clients = {
		new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false),
		new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 1.6, 100, true, true),
		new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 21, 1.78, 65, false, false),
		new ClientInfo("Rem Collier", ClientInfo.MALE, 49, 1.8, 120, false, true),
		new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 1.9, 75, true, false),
		new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 0.45, 1.6, false, false)
	};
}
