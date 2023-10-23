package service.core;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.*;

/**
 * Interface to define the behaviour of a quotation service.
 * 
 * @author Rem
 *
 */

@WebService
public interface QuotationService {
	@WebMethod
	public Quotation generateQuotation(ClientInfo info);
}
