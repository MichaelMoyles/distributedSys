import service.broker.LocalBrokerService;
import service.core.Quotation;
import service.core.QuotationService;
import service.core.BrokerService;
import service.core.ClientInfo;
import javax.xml.ws.Endpoint;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class LocalBrokerServiceUnitTest {
    @BeforeClass
    public static void setup() {
        Endpoint.publish("http://0.0.0.0:9000/broker", new LocalBrokerService());
    }

    @Test
    public void connectionTest() throws Exception {
        URL wsdlUrl = new URL("http://localhost:9000/broker?wsdl");
        QName serviceName = new QName("http://core.service/", "BrokerService");
        Service service = Service.create(wsdlUrl, serviceName);
        QName portName = new QName("http://core.service/", "BrokerServicePort");
        BrokerService brokerService = service.getPort(portName, BrokerService.class);

        ClientInfo clientInfo = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false);
        int res = brokerService.getQuotations(clientInfo).size();
        assertEquals(0, res);
    }
}