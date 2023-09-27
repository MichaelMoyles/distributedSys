import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry; 
import java.rmi.server.UnicastRemoteObject;
import service.core.Constants; 
import service.core.QuotationService; 
import service.core.ClientInfo; 
import service.core.Quotation; 
import service.broker.LocalBrokerService;
import service.core.BrokerService;
import org.junit.*;
import static org.junit.Assert.assertNotNull;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class LocalBrokerUnitTest { 
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        BrokerService localBrokerService = new LocalBrokerService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            BrokerService brokerService = (BrokerService) UnicastRemoteObject.exportObject(localBrokerService,0);
            registry.bind(Constants.BROKER_SERVICE, brokerService); } catch (Exception e) {
                System.out.println("Trouble: " + e); 
        }
    }

    @Test
    public void connectionTest() throws Exception {
        BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void quotationsTest() throws Exception {
        Registry registry = LocateRegistry.getRegistry(1099);
        for (String name : registry.list()) {
            System.out.println(name);
        }
        assertNotNull(registry);
    }
}