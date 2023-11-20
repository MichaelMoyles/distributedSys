package service.broker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RegistryCreation implements CommandLineRunner {
	private final ServiceRegistry serviceRegistry;

	public RegistryCreation(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public void run(String... args) {
		serviceRegistry.registerService("http://dodgygeezers:8082/quotations");
		serviceRegistry.registerService("http://auldfellas:8080/quotations");
		serviceRegistry.registerService("http://girlsallowed:8081/quotations");
	}
}
