package at.fontain.liveness.health;

import org.junit.jupiter.api.Test;
import org.springframework.boot.health.contributor.Health;

import static org.assertj.core.api.Assertions.assertThat;

class CustomHealthIndicatorTest {

    private final CustomHealthIndicator indicator = new CustomHealthIndicator();

    @Test
    void health_defaultIsUp() {
        Health health = indicator.health();
        assertThat(health.getStatus().getCode()).isEqualTo("UP");
        assertThat(health.getDetails()).containsEntry("custom", "Everything OK");
    }

    @Test
    void health_whenSetToFalse_isDown() {
        indicator.setHealthy(false);

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("DOWN");
        assertThat(health.getDetails()).containsEntry("custom", "Simulated failure");
    }

    @Test
    void health_afterTogglingBackToTrue_isUp() {
        indicator.setHealthy(false);
        indicator.setHealthy(true);

        Health health = indicator.health();

        assertThat(health.getStatus().getCode()).isEqualTo("UP");
    }
}
