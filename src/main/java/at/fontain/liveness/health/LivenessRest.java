package at.fontain.liveness.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.health.actuate.endpoint.HealthEndpointGroups;
import org.springframework.boot.health.registry.HealthContributorRegistry;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Health Simulation", description = "Toggle liveness, readiness, and custom health probe states")
public class LivenessRest {
    private static final Logger log = LoggerFactory.getLogger(LivenessRest.class);

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
    }

    @GetMapping("/simulate/readiness/{state}")
    @Operation(summary = "Toggle readiness probe", description = "Set to true (ACCEPTING_TRAFFIC) or false (REFUSING_TRAFFIC)")
    public String setReadiness(@PathVariable boolean state) {
        log.info("setReadiness({})", state);
        readiness.setReady(state);
        return "Readiness set to " + state;
    }

    @GetMapping("/simulate/liveness/{state}")
    @Operation(summary = "Toggle liveness probe", description = "Set to true (CORRECT) or false (BROKEN)")
    public String setLiveness(@PathVariable boolean state) {
        log.info("setLiveness({})", state);
        liveness.setLive(state);
        return "Liveness set to " + state;
    }

    @GetMapping("/simulate/health/{state}")
    @Operation(summary = "Toggle custom health indicator", description = "Set to true (UP) or false (DOWN)")
    public String setHealth(@PathVariable boolean state) {
        log.info("setHealth({})", state);
        health.setHealthy(state);
        return "Health set to " + state;
    }

    @GetMapping("/")
    @Operation(summary = "Instance info", description = "Returns hostname, IP, port, and current health states")
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

    @GetMapping("favicon.ico")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ignoreFavicon() {
        log.info("favicon.ico requested, returning 204 No Content");
    }
}
