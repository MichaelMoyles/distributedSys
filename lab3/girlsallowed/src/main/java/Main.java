import service.girlsallowed.GAQService;
import javax.xml.ws.Endpoint;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.net.InetAddress;

public class Main {
    public static void main(String[] args) {
        String hostname = "localhost";
        if (args.length > 0) {
            hostname = args[0];
        }
        Endpoint.publish("http://0.0.0.0:9003/quotations", new GAQService());
        jmdnsAdvertise(hostname);
    }

    private static void jmdnsAdvertise(String host) {
        try {
            String config = "path=http://"+host+":9003/quotations?wsdl";
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "ws-service", 9003, config);
            jmdns.registerService(serviceInfo);

            Thread.sleep(100000);

            jmdns.unregisterAllServices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}