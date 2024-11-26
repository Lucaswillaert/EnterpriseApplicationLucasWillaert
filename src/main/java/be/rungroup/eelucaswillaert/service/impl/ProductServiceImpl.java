package be.rungroup.eelucaswillaert.service.impl;

import be.rungroup.eelucaswillaert.dto.ProductDto;
import be.rungroup.eelucaswillaert.model.Product;
import be.rungroup.eelucaswillaert.repository.ProductRepository;
import be.rungroup.eelucaswillaert.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    private ProductRepository productrepository;



    //returnt een lijst van alle producten
    @Override
    public List<Product> findAllProducts() {
        List<Product> products = productrepository.findAll();
        return products.stream().map(product -> mapToProductDto(product)).collect(Collectors.toList());
    }


    //slaat een product op
    @Override
    public Product saveProduct(ProductDto productDto) {
        Product product = mapToProduct(productDto);
        return productrepository.save(product);
    }

    @Override
    public ProductDto findById(Long id) {
        return null;
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

    //mapt een model naar een Data Transfer Object
    public static Product mapToProductDto(Product product) {
        return Product.builder()
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
