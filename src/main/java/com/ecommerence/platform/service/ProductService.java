package com.ecommerence.platform.service;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.dto.ProductDto;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.ProductsResponse;
import com.ecommerence.platform.util.SortRequestBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Product product = new Product();
        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());

        product.setCreatedDate(new Date());

        productRepository.save(product);

        return productDto;
    }


    @Override
    public void deleteProduct(Integer id) throws ProductNotFoundException {

        if(!productRepository.existsById(id) ){
            throw new ProductNotFoundException(String.format(AppConstants.PRODUCT_WITH_ID_NOT_FOUND_MESSAGE_TEMPLATE, id));
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto updateProduct(Integer id, ProductDto productDto) throws ProductNotFoundException {

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format(AppConstants.PRODUCT_WITH_ID_NOT_FOUND_MESSAGE_TEMPLATE, id)));

        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());
        product.setUpdatedDate(new Date());

        productRepository.save(product);

        return productDto;
    }

    @Override
    public ProductsResponse getProducts(ProductOrderEnum orderBy, DirectionEnum direction, Integer page, Integer pageSize) {

        Pageable pageAndSortingRequest = PageRequest.of(page, pageSize, SortRequestBuilder.build(orderBy, direction));

        Page<Product> currentPageProducts = productRepository.findAll(pageAndSortingRequest);

        return new ProductsResponse(currentPageProducts.getContent(), currentPageProducts.getTotalElements());
    }
}
