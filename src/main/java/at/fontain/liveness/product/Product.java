package at.fontain.liveness.product;

import java.util.UUID;

public record Product(
        UUID id,
        String name,
        double price
) {
}
