package be.rungroup.eelucaswillaert.repository;

import be.rungroup.eelucaswillaert.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    Optional <Product> addToCart(Long id, int quantity);

    Optional <Product> removeFromCart(Long id, int quantity);


}
