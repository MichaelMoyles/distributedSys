import service.auldfellas.AFQService;
import service.core.Quotation;
import service.message.ClientMessage;
import service.message.QuotationMessage;

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

public class Main {
    private static AFQService service = new AFQService();
    public static void main(String[] args) {
        try {
            String host = "localhost";
            if (args.length > 0) {
                host = args[0];
            }
            ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
            Connection connection = factory.createConnection();
            connection.setClientID("auldfellas");
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = session.createQueue("QUOTATIONS");
            Topic topic = session.createTopic("APPLICATIONS");
            MessageConsumer consumer = session.createConsumer(topic);
            MessageProducer producer = session.createProducer(queue);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        ClientMessage request = (ClientMessage) ((ObjectMessage) message).getObject();
                        Quotation quotation = service.generateQuotation(request.getInfo());
                        Message response = session.createObjectMessage(new QuotationMessage(request.getToken(), quotation));
                        producer.send(response);
                        message.acknowledge();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            connection.start();
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
}