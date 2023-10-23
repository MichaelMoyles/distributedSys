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
import java.util.LinkedList;
import service.message.OfferMessage;
import service.message.QuotationMessage;
import service.message.ClientMessage;

public class Main {

    static HashMap<Long, OfferMessage> store = new HashMap<>();

    public static void main(String[] args) {
        try {
            String host = "localhost";
            if (args.length > 0) {
                host = args[0];
            }
            ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
            Connection connection = factory.createConnection();
            connection.setClientID("BrokerService");
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            Topic appTopic = session.createTopic("APPLICATIONS");
            Queue quoteQueue = session.createQueue("QUOTATIONS");
            Queue offerQueue = session.createQueue("OFFERS");

            MessageConsumer appConsumer = session.createConsumer(appTopic);
            MessageConsumer quoteConsumer = session.createConsumer(quoteQueue);
            MessageProducer offersProducer = session.createProducer(offerQueue);

            connection.start();

            new Thread(() -> {
                while(true) {
                    try {
                        Message recMessage = appConsumer.receive();
                        if (recMessage instanceof ObjectMessage) {
                            Object serviceMessage = ((ObjectMessage) recMessage).getObject();
                            if (serviceMessage instanceof ClientMessage) {
                                ClientMessage clientMessage = (ClientMessage) serviceMessage;

                                OfferMessage serviceOffer = new OfferMessage(clientMessage.getInfo(), new LinkedList<>());
                                store.put(clientMessage.getToken(), serviceOffer);

                                new Thread(() -> {
                                    try {
                                        Thread.sleep(3000);
                                        Message offerMessage = session.createObjectMessage(store.get(clientMessage.getToken()));
                                        offersProducer.send(offerMessage);
                                        store.remove(clientMessage.getToken());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                while(true) {
                    try {
                        Message recMessage = quoteConsumer.receive();
                        if (recMessage instanceof ObjectMessage) {
                            Object serviceMessage = ((ObjectMessage) recMessage).getObject();
                            if (serviceMessage instanceof QuotationMessage) {
                                QuotationMessage quotationMessage = (QuotationMessage) serviceMessage;
                                if (store.containsKey(quotationMessage.getToken())) {
                                    OfferMessage offer = store.get(quotationMessage.getToken());
                                    offer.getQuotations().add(quotationMessage.getQuotation());
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}