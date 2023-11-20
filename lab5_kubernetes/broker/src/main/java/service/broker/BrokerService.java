package service.broker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import service.core.Application;
import service.core.ClientInfo;
import service.core.Quotation;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
public class BrokerService {

    private String[] arrURLs = {
        "http://auldfellas:8080/quotations",
        "http://girlsallowed:8081/quotations",
        "http://dodgygeezers:8082/quotations"
    };

    private Map<Integer, Application> applications = new HashMap<>();
    private ServiceRegistry serviceRegistry;

    @Autowired
	public BrokerService(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

    @PostMapping("/applications")
    public ResponseEntity<Application> createApplication(@RequestBody ClientInfo info) {
        RestTemplate template = new RestTemplate();
        Application application = new Application(info);

        for (String url : arrURLs) {
            try {
                ResponseEntity<Quotation> response = template.postForEntity(url, info, Quotation.class);
                if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                    application.getQuotations().add(response.getBody());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        applications.put(application.id, application);
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }


    @GetMapping("/applications")
    @ResponseBody
    public ResponseEntity<Map<Integer, Application>> getApplications() {
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<Application> getApplication(@PathVariable int id) {
        return ResponseEntity.ok(applications.get(id));
    }

    @PostMapping("/services")
	public void registerService(@RequestBody String serviceUrl) {
		serviceRegistry.registerService(serviceUrl);
	}

	@GetMapping("/services")
	public List<String> getRegisteredServices() {
		return serviceRegistry.getRegisteredServices();
	}
}
