import service.auldfellas.AFQService;
import service.core.Quotation;
import service.core.ClientInfo;
import org.junit.*;

import static org.junit.Assert.assertEquals;

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

public class AuldfellasUnitTest {
    @Test
    public void testService() throws Exception {
        Main.main(new String[0]);
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://localhost:61616");
        Connection connection = factory.createConnection();
        connection.setClientID("test");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("QUOTATIONS");
        Topic topic = session.createTopic("APPLICATIONS");
        MessageConsumer consumer = session.createConsumer(queue);
        MessageProducer producer = session.createProducer(topic);
        connection.start();
        producer.send(session.createObjectMessage(new ClientMessage(1L, new ClientInfo("Niki Collier",
                ClientInfo.FEMALE, 49, 1.5494, 80, false, false))));
        Message message = consumer.receive();
        QuotationMessage quotationMessage = (QuotationMessage) ((ObjectMessage) message).getObject();
        System.out.println("token: " + quotationMessage.getToken());
        System.out.println("quotation: " + quotationMessage.getQuotation());
        message.acknowledge();
        assertEquals(1L, quotationMessage.getToken());
    }
}