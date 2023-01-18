package com.ecommerence.platform.controller;

import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.model.Category;
import com.ecommerence.platform.response.ProductsResponse;
import com.ecommerence.platform.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<ProductsResponse> getAllProducts(@RequestParam("orderBy") ProductOrderEnum orderBy,
                                                           @RequestParam("direction") DirectionEnum direction,
                                                           @RequestParam("page") Integer page,
                                                           @RequestParam("pageSize") Integer pageSize) {

        ProductsResponse productsResponse = productService.getProducts(orderBy, direction, page, pageSize);

        return new ResponseEntity<>(productsResponse, HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getProductsAvailablePerCategories() {
        List<Category> categoriesWithProductsAvailableList = productService.getProductsAvailablePerCategories();

        return new ResponseEntity<>(categoriesWithProductsAvailableList, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        productService.createProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("Product %s created", product.getName()));
    }


    @PostMapping("{id}/order/{quantity}")
    public ResponseEntity<String> orderProduct(@PathVariable("id") Integer id, @PathVariable("quantity") Integer quantity) throws Exception {

        String orderMessage = productService.orderProduct(id, quantity);

        return ResponseEntity.status(HttpStatus.OK)
                .body(String.format(orderMessage));
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
