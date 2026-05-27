package at.fontain.liveness.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Test
    void getAll_delegatesToRepository() {
        List<Product> products = List.of(new Product(ID, "A", 1.0));
        when(repository.findAll()).thenReturn(products);

        assertThat(service.getAll()).isEqualTo(products);
        verify(repository).findAll();
    }

    @Test
    void getById_existingId_returnsProduct() {
        Product product = new Product(ID, "A", 1.0);
        when(repository.findById(ID)).thenReturn(Optional.of(product));

        assertThat(service.getById(ID)).isEqualTo(product);
    }

    @Test
    void getById_unknownId_throwsProductNotFoundException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(ID))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ID.toString());
    }

    @Test
    void create_savesProductWithGeneratedId() {
        ProductRequest request = new ProductRequest("New Product", 9.99);
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product created = service.create(request);

        assertThat(created.name()).isEqualTo("New Product");
        assertThat(created.price()).isEqualTo(9.99);
        assertThat(created.id()).isNotNull();
        verify(repository).save(created);
    }

    @Test
    void update_existingId_savesAndReturnsUpdatedProduct() {
        ProductRequest request = new ProductRequest("Updated", 19.99);
        Product updated = new Product(ID, "Updated", 19.99);
        when(repository.existsById(ID)).thenReturn(true);
        when(repository.save(any(Product.class))).thenReturn(updated);

        Product result = service.update(ID, request);

        assertThat(result).isEqualTo(updated);
        verify(repository).save(new Product(ID, "Updated", 19.99));
    }

    @Test
    void update_unknownId_throwsProductNotFoundException() {
        when(repository.existsById(ID)).thenReturn(false);

        assertThatThrownBy(() -> service.update(ID, new ProductRequest("X", 1.0)))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ID.toString());
        verify(repository, never()).save(any());
    }

    @Test
    void delete_existingId_callsDeleteById() {
        when(repository.existsById(ID)).thenReturn(true);

        service.delete(ID);

        verify(repository).deleteById(ID);
    }

    @Test
    void delete_unknownId_throwsProductNotFoundException() {
        when(repository.existsById(ID)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(ID))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ID.toString());
        verify(repository, never()).deleteById(any());
    }
}
