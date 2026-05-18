package at.fontain.liveness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class CustomHealthIndicator implements HealthIndicator {
    private static final Logger log = LoggerFactory.getLogger(CustomHealthIndicator.class);

    private final AtomicBoolean healthy = new AtomicBoolean(true);

    @Override
    public Health health() {
        log.info("health() called, healthy={}", healthy.get());

        if (healthy.get()) {
            return Health.up().withDetail("custom", "Everything OK").build();
        } else {
            return Health.down().withDetail("custom", "Simulated failure").build();
        }
    }

    public void setHealthy(boolean value) {
        healthy.set(value);
    }
}
