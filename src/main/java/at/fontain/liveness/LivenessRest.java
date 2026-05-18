package at.fontain.liveness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthEndpointGroups;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LivenessRest {
    private static final Logger log = LoggerFactory.getLogger(LivenessRest.class);

    private final ToggleableReadinessIndicator readiness;
    private final ToggleableLivenessIndicator liveness;
    private final CustomHealthIndicator health;
    private final HealthEndpointGroups groups;
    private final HealthContributorRegistry registry;
    private final ServerProperties properties;

    public LivenessRest(ToggleableReadinessIndicator readiness,
                        ToggleableLivenessIndicator liveness,
                        CustomHealthIndicator health,
                        HealthEndpointGroups groups,
                        HealthContributorRegistry registry,
                        ServerProperties properties) {
        this.readiness = readiness;
        this.liveness = liveness;
        this.health = health;
        this.groups = groups;
        this.registry = registry;
        this.properties = properties;
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
    public List<String> root() {
        String hostname = properties.getAddress().getHostName();
        String hostaddress = properties.getAddress().getHostAddress();
        int port = properties.getPort();
        List<String> list = new ArrayList<>();
        list.add("hostname: " + hostname);
        list.add("hostaddress: " + hostaddress);
        list.add("port: " + port);
        return list;
    }
}
