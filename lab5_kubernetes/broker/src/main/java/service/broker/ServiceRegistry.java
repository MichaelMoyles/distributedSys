package service.broker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceRegistry {
	private final List<String> serviceUrls = new ArrayList<>();

	public void registerService(String serviceUrl) {
		serviceUrls.add(serviceUrl);
	}

	public List<String> getRegisteredServices() {
		return serviceUrls;
	}
}