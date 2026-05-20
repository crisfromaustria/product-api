package at.fontain.liveness;

import java.util.UUID;

public record Product(
        UUID id,
        String name,
        double price
) {
}
