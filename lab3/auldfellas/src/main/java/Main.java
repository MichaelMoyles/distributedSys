import service.auldfellas.AFQService;
import javax.xml.ws.Endpoint;
import java.net.URL;
import java.util.List;
import java.util.LinkedList;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.net.InetAddress;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        if (args.length > 0) {
            host = args[0];
        }
        Endpoint.publish("http://0.0.0.0:9001/quotations", new AFQService());
        jmdnsAdvertise(host);
    }

    private static void jmdnsAdvertise(String host) {
        try {
            String config = "path=http://"+host+":9001/quotations?wsdl";
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "ws-service", 9001, config);
            jmdns.registerService(serviceInfo);

            Thread.sleep(100000);

            jmdns.unregisterAllServices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}