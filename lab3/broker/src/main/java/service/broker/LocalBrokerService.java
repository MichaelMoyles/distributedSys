package service.broker;

import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.*;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
@WebService(name="BrokerService", targetNamespace="http://core.service/", serviceName="BrokerService")
@SOAPBinding(style = Style.DOCUMENT, use= Use.LITERAL)
public class LocalBrokerService implements BrokerService {

	public List<URL> urls;

	public LocalBrokerService() {this.urls = new ArrayList<>();};

	@WebMethod
	public LinkedList<Quotation> getQuotations(ClientInfo info) {
		LinkedList<Quotation> quotations = new LinkedList<Quotation>();

		for (URL url : urls) {
			try {
				QName serviceName = new QName("http://core.service/", "QuotationService");
				Service service = Service.create(url, serviceName);
				QName portName = new QName("http://core.service/", "QuotationServicePort");
				QuotationService quotationService = service.getPort(portName, QuotationService.class);
				Quotation quotation = quotationService.generateQuotation(info);
				quotations.add(quotation);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return quotations;
	}
}
