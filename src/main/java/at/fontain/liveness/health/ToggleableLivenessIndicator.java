package at.fontain.liveness.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.health.application.LivenessStateHealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component("livenessStateHealthIndicator")
public class ToggleableLivenessIndicator extends LivenessStateHealthIndicator {
    private static final Logger log = LoggerFactory.getLogger(ToggleableLivenessIndicator.class);

    private final AtomicBoolean live = new AtomicBoolean(true);

    public ToggleableLivenessIndicator(ApplicationAvailability availability) {
        super(availability);
    }

    @Override
    public AvailabilityState getState(ApplicationAvailability applicationAvailability) {
        log.info("getState() called, live={}", live.get());

        AvailabilityState availabilityState = super.getState(applicationAvailability);
        log.info("super.getState() returned {}", availabilityState);

        return live.get() ? availabilityState : LivenessState.BROKEN;
    }

    public void setLive(boolean value) {
        live.set(value);
    }
}
