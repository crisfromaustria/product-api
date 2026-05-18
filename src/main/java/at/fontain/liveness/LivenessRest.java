package at.fontain.liveness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthEndpointGroups;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulate")
public class LivenessRest {
    private static final Logger log = LoggerFactory.getLogger(LivenessRest.class);

    private final ToggleableReadinessIndicator readiness;
    private final ToggleableLivenessIndicator liveness;
    private final CustomHealthIndicator health;
    private final HealthEndpointGroups groups;
    private final HealthContributorRegistry registry;

    public LivenessRest(ToggleableReadinessIndicator readiness,
                        ToggleableLivenessIndicator liveness,
                        CustomHealthIndicator health,
                        HealthEndpointGroups groups,
                        HealthContributorRegistry registry) {
        this.readiness = readiness;
        this.liveness = liveness;
        this.health = health;
        this.groups = groups;
        this.registry = registry;
    }

    @GetMapping("/readiness/{state}")
    public String setReadiness(@PathVariable boolean state) {
        log.info("setReadiness({})", state);

        readiness.setReady(state);
        return "Readiness set to " + state;
    }

    @GetMapping("/liveness/{state}")
    public String setLiveness(@PathVariable boolean state) {
        log.info("setLiveness({})", state);

        liveness.setLive(state);
        return "Liveness set to " + state;
    }

    @GetMapping("/health/{state}")
    public String setHealth(@PathVariable boolean state) {
        log.info("setHealth({})", state);

        health.setHealthy(state);
        return "Health set to " + state;
    }
}
