package be.rungroup.eelucaswillaert.service;

import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProductService {
    List<Product> findAllProducts();

    Product saveProduct(ProductDto product);

    ProductDto findById(Long id);

    void updateProduct(ProductDto club);

    void deleteProduct(long id);

    List<ProductDto> searchProducts(String query);

    boolean isProductAvailable(long id);
}
