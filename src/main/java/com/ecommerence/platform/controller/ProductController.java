package com.ecommerence.platform.controller;

import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        productService.createProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("Product %s created", product.getName()));
    }


    @PutMapping
    public ResponseEntity<String> updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Product updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProduct(@RequestParam("id") Integer id) {
        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Product deleted");
    }
}
