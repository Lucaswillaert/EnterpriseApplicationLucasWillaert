package be.rungroup.eelucaswillaert.service.impl;

import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.repository.ProductRepository;
import be.rungroup.eelucaswillaert.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productrepository;


    @Autowired
    public ProductServiceImpl(ProductRepository productrepository) {
        this.productrepository = productrepository;
    }

    //return een lijst van alle producten
    @Override
    public List<ProductDto> findAllProducts() {
        List<Product> products = productrepository.findAll();
        return products.stream().map(ProductServiceImpl::mapToProductDto).collect(Collectors.toList());
    }

    //slaat een product op
    @Override
    public void saveProduct(ProductDto productDto) {
        Product product = mapToProduct(productDto);
        productrepository.save(product);
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productrepository.findById(id).orElseThrow(()->new RuntimeException("Product not found"));
        return mapToProductDto(product); //mapt een model naar een Data Transfer Object
    }

    @Override
    public void updateProduct(ProductDto product) {
        Product productToUpdate = mapToProduct(product);
        productrepository.save(productToUpdate);

    }

    @Override
    public void deleteProduct(long id) {
        productrepository.deleteById(id);

    }

    @Override
    public List<ProductDto> searchProducts(String query) {
        return List.of();
    }

    @Override
    public boolean isProductAvailable(long id) {
        Product product = productrepository.findById(id).get();

        return product.getTotalStock() > 0;
    }

    //mapt een model naar een Data Transfer Object
    public static ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .tags(product.getTags())
                .photoUrl(product.getPhotoUrl())
                .totalStock(product.getTotalStock())
                .build();
    }

    //mapt een Data Transfer Object naar een model
    public static Product mapToProduct(ProductDto product) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .tags(product.getTags())
                .photoUrl(product.getPhotoUrl())
                .totalStock(product.getTotalStock())
                .build();
    }
}
