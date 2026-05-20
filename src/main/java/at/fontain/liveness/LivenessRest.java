package at.fontain.liveness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.health.actuate.endpoint.HealthEndpointGroups;
import org.springframework.boot.health.registry.HealthContributorRegistry;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class LivenessRest {
    private static final Logger log = LoggerFactory.getLogger(LivenessRest.class);

    private final List<Product> productList;

    private final ToggleableReadinessIndicator readiness;
    private final ToggleableLivenessIndicator liveness;
    private final CustomHealthIndicator health;
    private final HealthEndpointGroups groups;
    private final HealthContributorRegistry registry;
    private final ServletWebServerApplicationContext webServerContext;

    public LivenessRest(ToggleableReadinessIndicator readiness,
                        ToggleableLivenessIndicator liveness,
                        CustomHealthIndicator health,
                        HealthEndpointGroups groups,
                        HealthContributorRegistry registry,
                        ServletWebServerApplicationContext webServerContext) {
        this.readiness = readiness;
        this.liveness = liveness;
        this.health = health;
        this.groups = groups;
        this.registry = registry;
        this.webServerContext = webServerContext;
        this.productList = new ArrayList<>();
        init();
    }

    private void init() {
        productList.add(new Product(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Product 1", 10.0));
        productList.add(new Product(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Product 2", 20.0));
        productList.add(new Product(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Product 3", 30.0));
    }

    @GetMapping("/simulate/readiness/{state}")
    public String setReadiness(@PathVariable boolean state) {
        log.info("setReadiness({})", state);

        readiness.setReady(state);
        return "Readiness set to " + state;
    }

    @GetMapping("/simulate/liveness/{state}")
    public String setLiveness(@PathVariable boolean state) {
        log.info("setLiveness({})", state);

        liveness.setLive(state);
        return "Liveness set to " + state;
    }

    @GetMapping("/simulate/health/{state}")
    public String setHealth(@PathVariable boolean state) {
        log.info("setHealth({})", state);

        health.setHealthy(state);
        return "Health set to " + state;
    }

    @GetMapping("/")
    public List<String> root() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        int port = webServerContext.getWebServer().getPort();
        List<String> list = new ArrayList<>();
        list.add("hostname: " + localHost.getHostName());
        list.add("hostaddress: " + localHost.getHostAddress());
        list.add("port: " + port);
        list.add("liveness: " + liveness.health());
        list.add("readiness: " + readiness.health());
        list.add("custom health: " + health.health());
        return list;
    }

    @GetMapping("/products")
    public List<Product> getProductList() {
        return this.productList;
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable UUID id) {
        return productList.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
