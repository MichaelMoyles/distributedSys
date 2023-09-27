package service.broker;

import java.util.LinkedList;
import java.util.List;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
public class LocalBrokerService implements BrokerService {
	public List<Quotation> getQuotations(ClientInfo info) {
		List<Quotation> quotations = new LinkedList<Quotation>();

		try {
			Registry registry = LocateRegistry.getRegistry(1099);

			for (String name : registry.list()) {
				if (name.startsWith("qs-")) {
					try {
						QuotationService service = (QuotationService) registry.lookup(name);
                        quotations.add(service.generateQuotation(info));
					} catch (NotBoundException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return quotations;
	}

	public void registerService(String name, java.rmi.Remote service) {
		try {
		Registry registry = LocateRegistry.getRegistry(1099);
			registry.bind(name, service);
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
	}

}
