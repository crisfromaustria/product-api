package at.fontain.liveness.product;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepository {

    private final Map<UUID, Product> store = new ConcurrentHashMap<>();

    public ProductRepository() {
        save(new Product(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Product 1", 10.0));
        save(new Product(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Product 2", 20.0));
        save(new Product(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Product 3", 30.0));
    }

    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    public Product save(Product product) {
        store.put(product.id(), product);
        return product;
    }

    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }

    public void deleteById(UUID id) {
        store.remove(id);
    }
}
