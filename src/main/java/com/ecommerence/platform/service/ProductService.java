package com.ecommerence.platform.service;

import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductService {

    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String createProduct(Product product) {

        product.setCreatedDate(new Date());
        productRepository.save(product);

        return "Success";
    }

    public String deleteProduct(Integer id) {
        productRepository.deleteById(id);

        return "Success";
    }

    public String updateProduct(Product product) {
        product.setUpdatedDate(new Date());
        productRepository.save(product);

        return "Success";
    }
}
