import service.broker.LocalBrokerService;
import javax.xml.ws.Endpoint;

import java.util.LinkedList;
import java.util.List;

import javax.jmdns.ServiceInfo;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.UnknownHostException;
import service.core.QuotationService;

public class Main {
    public static List<URL> urls = new LinkedList<URL>();
    static LocalBrokerService localBrokerService = new LocalBrokerService();

    public static List<URL> getUrls() {
        return urls;
    }

    public static void main(String[] args) {
        Endpoint.publish("http://0.0.0.0:9000/broker", localBrokerService);
        JmdnsClient client= new JmdnsClient();
        try {
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            jmdns.addServiceListener("_http._tcp.local.", client);
            Thread.sleep(30000);
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class JmdnsClient implements ServiceListener {
        @Override
        public void serviceAdded(ServiceEvent event) {
            System.out.println("Service added: " + event.getInfo());
        }
        @Override
        public void serviceRemoved(ServiceEvent event) {
            System.out.println("Service removed: " + event.getInfo());
        }
        @Override
        public void serviceResolved(ServiceEvent event) {
            System.out.println("Service resolved: " + event.getInfo());
            String path = event.getInfo().getPropertyString("path");
            if (path != null) {
                try {
                    URL serviceURL = new URL(path);
                    localBrokerService.urls.add(serviceURL);

                } catch (Exception e) {
                    System.out.println("Problem with service: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}