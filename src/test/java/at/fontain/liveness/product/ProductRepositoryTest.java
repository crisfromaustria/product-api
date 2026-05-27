package at.fontain.liveness.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryTest {

    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ProductRepository();
    }

    @Test
    void findAll_returnsThreePreloadedProducts() {
        List<Product> products = repository.findAll();
        assertThat(products).hasSize(3);
    }

    @Test
    void findById_existingId_returnsProduct() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Optional<Product> result = repository.findById(id);
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("Product 1");
        assertThat(result.get().price()).isEqualTo(10.0);
    }

    @Test
    void findById_unknownId_returnsEmpty() {
        Optional<Product> result = repository.findById(UUID.randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void save_addsNewProductAndReturnsIt() {
        UUID id = UUID.randomUUID();
        Product product = new Product(id, "New Product", 99.9);

        Product saved = repository.save(product);

        assertThat(saved).isEqualTo(product);
        assertThat(repository.findById(id)).isPresent();
        assertThat(repository.findAll()).hasSize(4);
    }

    @Test
    void save_existingId_overwritesProduct() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Product updated = new Product(id, "Updated", 99.0);

        repository.save(updated);

        assertThat(repository.findById(id).get().name()).isEqualTo("Updated");
        assertThat(repository.findAll()).hasSize(3);
    }

    @Test
    void existsById_existingId_returnsTrue() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000002");
        assertThat(repository.existsById(id)).isTrue();
    }

    @Test
    void existsById_unknownId_returnsFalse() {
        assertThat(repository.existsById(UUID.randomUUID())).isFalse();
    }

    @Test
    void deleteById_removesProduct() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");
        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
        assertThat(repository.existsById(id)).isFalse();
        assertThat(repository.findAll()).hasSize(2);
    }
}
