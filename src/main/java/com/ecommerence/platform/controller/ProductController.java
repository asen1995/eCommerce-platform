package com.ecommerence.platform.controller;

import com.ecommerence.platform.dto.ProductDto;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.response.ProductsResponse;
import com.ecommerence.platform.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<ProductsResponse> getAllProducts(@RequestParam(value = "orderBy", defaultValue = "NAME") ProductOrderEnum orderBy,
                                                           @RequestParam(value = "direction", defaultValue = "ASC") DirectionEnum direction,
                                                           @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        ProductsResponse productsResponse = productService.getProducts(orderBy, direction, page, pageSize);

        return new ResponseEntity<>(productsResponse, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto productDto) {

        ProductDto createdProduct = productService.createProduct(productDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    @PostMapping("/add-many")
    public ResponseEntity<List<ProductDto>> create(@Valid @RequestBody List<ProductDto> productDtos) {

        List<ProductDto> createdProducts = productService.createOrUpdateProducts(productDtos);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdProducts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable("id") Integer id, @Valid @RequestBody ProductDto product) throws ProductNotFoundException {

        ProductDto updatedProduct = productService.updateProduct(id, product);

        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) throws ProductNotFoundException {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
