package at.fontain.liveness.product;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product create(ProductRequest request) {
        Product product = new Product(UUID.randomUUID(), request.name(), request.price());
        return repository.save(product);
    }

    public Product update(UUID id, ProductRequest request) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        return repository.save(new Product(id, request.name(), request.price()));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
