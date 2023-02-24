package com.ecommerence.platform.service;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.dto.ProductDto;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.mapper.ProductMapper;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.ProductsResponse;
import com.ecommerence.platform.util.SortRequestBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class ProductService implements IProductService {

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        logger.info("Creating product with name: {}", productDto.getName());
        Product product = productMapper.toProductEntity(productDto);
        product.setCreatedDate(new Date());

        productRepository.save(product);

        logger.info("Product with name: {} created successfully", productDto.getName());
        return productDto;
    }

    @Override
    @Transactional
    public List<ProductDto> createOrUpdateProducts(List<ProductDto> productDtoList) {

        logger.info("Creating or updating products started for {} products", productDtoList.size());
        productDtoList.stream()
                .map(productDto -> {
                    Product product = productMapper.toProductEntity(productDto);
                    product.setCreatedDate(new Date());
                    return product;
                })
                .forEach(productRepository::upsert);

        logger.info("Creating or updating products finished successfully ");
        return productDtoList;
    }

    @Override
    public void deleteProduct(Integer id) throws ProductNotFoundException {

        logger.info("Deleting product with id: {}", id);

        if (!productRepository.existsById(id)) {
            logger.error("Product with id: {} not found", id);
            throw new ProductNotFoundException(String.format(AppConstants.PRODUCT_WITH_ID_NOT_FOUND_MESSAGE_TEMPLATE, id));
        }

        productRepository.deleteById(id);
        logger.info("Product with id: {} deleted successfully", id);
    }

    @Override
    public ProductDto updateProduct(Integer id, ProductDto productDto) throws ProductNotFoundException {

        logger.info("Updating product with id: {}", id);

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format(AppConstants.PRODUCT_WITH_ID_NOT_FOUND_MESSAGE_TEMPLATE, id)));

        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());
        product.setUpdatedDate(new Date());

        productRepository.save(product);

        logger.info("Product with id: {} updated successfully", id);
        return productDto;
    }

    @Override
    public ProductsResponse getProducts(ProductOrderEnum orderBy, DirectionEnum direction, Integer page, Integer pageSize) {

        logger.info("Getting products with order by: {}, direction: {}, page: {}, pageSize: {}", orderBy, direction, page, pageSize);

        Pageable pageAndSortingRequest = PageRequest.of(page, pageSize, SortRequestBuilder.build(orderBy, direction));

        Page<Product> currentPageProducts = productRepository.findAll(pageAndSortingRequest);

        logger.info("{} products retrieved successfully", currentPageProducts.getTotalElements());
        return new ProductsResponse(currentPageProducts.getContent(), currentPageProducts.getTotalElements());
    }

}
