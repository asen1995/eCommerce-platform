package com.ecommerence.platform.controller;

import com.ecommerence.platform.dto.ProductDto;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.response.ProductsResponse;
import com.ecommerence.platform.service.IProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<ProductsResponse> getAllProducts(@RequestParam(value = "orderBy", defaultValue = "NAME") ProductOrderEnum orderBy,
                                                           @RequestParam(value = "direction", defaultValue = "ASC") DirectionEnum direction,
                                                           @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        logger.info("Getting all products with orderBy: {} and direction: {} and page: {} and pageSize: {}", orderBy, direction, page, pageSize);
        ProductsResponse productsResponse = productService.getProducts(orderBy, direction, page, pageSize);

        logger.info("GetAllProducts finished successfully");
        return new ResponseEntity<>(productsResponse, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto productDto) {

        logger.info("Creating Product starting...");
        ProductDto createdProduct = productService.createProduct(productDto);

        logger.info("Product created successfully");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    @PostMapping("/add-many")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'IMPORT_MANAGER')")
    public ResponseEntity<List<ProductDto>> create(@Valid @RequestBody List<ProductDto> productDtos) {

        logger.info("Creating list of products starting...");
        List<ProductDto> createdProducts = productService.createOrUpdateProducts(productDtos);

        logger.info("List of products created successfully");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdProducts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable("id") Integer id, @Valid @RequestBody ProductDto product) throws ProductNotFoundException {

        logger.info("Updating product with id: {}", id);
        ProductDto updatedProduct = productService.updateProduct(id, product);

        logger.info("Product updated successfully");
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) throws ProductNotFoundException {

        logger.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);

        logger.info("Product deleted successfully");
        return ResponseEntity.noContent().build();
    }
}
