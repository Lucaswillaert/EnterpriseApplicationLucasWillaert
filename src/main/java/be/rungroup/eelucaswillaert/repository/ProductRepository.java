package be.rungroup.eelucaswillaert.repository;

import be.rungroup.eelucaswillaert.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    Optional <Product> findById(Long id);

    void deleteById(Long id);


    @Query("SELECT p FROM Product p WHERE p.name LIKE CONCAT('%',:query,'%')")
    List<Product>searchProducts(String query);

}
