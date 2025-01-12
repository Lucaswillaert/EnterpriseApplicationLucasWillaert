package be.rungroup.eelucaswillaert.service;

import be.rungroup.eelucaswillaert.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProductService {
    List<ProductDto> findAllProducts();

    void saveProduct(ProductDto productDto);

    ProductDto findById(Long id);

    void deleteProduct(long id);

    List<ProductDto> searchProducts(String query);

    byte[] getProductPhoto(Long id);

    List<ProductDto> filterProductsByTags(List<String> tags);
}
