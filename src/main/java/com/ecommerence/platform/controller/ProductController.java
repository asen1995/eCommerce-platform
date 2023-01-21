package com.ecommerence.platform.controller;

import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.response.ProductsResponse;
import com.ecommerence.platform.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<ProductsResponse> getAllProducts(@RequestParam("orderBy") ProductOrderEnum orderBy,
                                                           @RequestParam("direction") DirectionEnum direction,
                                                           @RequestParam(value = "page" , defaultValue = "0") Integer page ,
                                                           @RequestParam(value = "pageSize" ,defaultValue = "5") Integer pageSize) {

        ProductsResponse productsResponse = productService.getProducts(orderBy, direction, page, pageSize);

        return new ResponseEntity<>(productsResponse, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid Product product) {
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
