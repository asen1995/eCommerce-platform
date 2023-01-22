package com.ecommerence.platform.service;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.ProductsResponse;
import com.ecommerence.platform.util.SortRequestBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String createProduct(Product product) {

        product.setCreatedDate(new Date());
        productRepository.save(product);

        return String.format(AppConstants.PRODUCT_CREATED_SUCCESSFULLY_MESSAGE, product.getName());
    }

    public String deleteProduct(Integer id) {
        productRepository.deleteById(id);

        return AppConstants.PRODUCT_DELETED_SUCCESSFULLY_MESSAGE;
    }

    public String updateProduct(Product product) {
        product.setUpdatedDate(new Date());
        productRepository.save(product);

        return AppConstants.PRODUCT_UPDATED_SUCCESSFULLY_MESSAGE;
    }

    public ProductsResponse getProducts(ProductOrderEnum orderBy, DirectionEnum direction, Integer page, Integer pageSize) {

        Pageable pageAndSortingRequest = PageRequest.of(page, pageSize, SortRequestBuilder.build(orderBy, direction));

        Page<Product> currentPageProducts = productRepository.findAll(pageAndSortingRequest);

        return new ProductsResponse(currentPageProducts.getContent(), currentPageProducts.getTotalElements());
    }
}
