package at.fontain.liveness.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotBlank String name,
        @Positive double price
) {
}
