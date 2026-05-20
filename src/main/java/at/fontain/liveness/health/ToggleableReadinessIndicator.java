package at.fontain.liveness.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.health.application.ReadinessStateHealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component("readinessStateHealthIndicator")
public class ToggleableReadinessIndicator extends ReadinessStateHealthIndicator {
    private static final Logger log = LoggerFactory.getLogger(ToggleableReadinessIndicator.class);

    private final AtomicBoolean ready = new AtomicBoolean(true);

    public ToggleableReadinessIndicator(ApplicationAvailability availability) {
        super(availability);
    }

    @Override
    public AvailabilityState getState(ApplicationAvailability applicationAvailability) {
        log.info("health() called, ready={}", ready.get());

        AvailabilityState availabilityState = super.getState(applicationAvailability);
        log.info("super.getState() returned {}", availabilityState);

        return ready.get() ? availabilityState : ReadinessState.REFUSING_TRAFFIC;
    }

    public void setReady(boolean value) {
        ready.set(value);
    }
}
