package com.ecommerence.platform.service;

import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.exception.ProductQuantityNotEnoughException;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.ProductsResponse;
import com.ecommerence.platform.util.SortRequestBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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

    public ProductsResponse getProducts(ProductOrderEnum orderBy, DirectionEnum direction, Integer page, Integer pageSize) {

        Pageable pageAndSortingRequest = PageRequest.of(page, pageSize, SortRequestBuilder.build(orderBy, direction));

        Page<Product> currentPageProducts = productRepository.findAll(pageAndSortingRequest);

        return new ProductsResponse(currentPageProducts.getContent(), currentPageProducts.getTotalElements());
    }

    public String orderProduct(Integer id, Integer orderedQuantity) throws Exception {

        Optional<Product> oProduct = productRepository.findById(id);

        if (!oProduct.isPresent()) {
            throw new ProductNotFoundException("Product not found");
        }

        Product product = oProduct.get();

        if (product.getQuantity() > orderedQuantity) {
            product.setQuantity(product.getQuantity() - orderedQuantity);
            productRepository.save(product);

            return String.format("You successfully ordered %s %s", orderedQuantity, product.getName());
        } else {
            throw new ProductQuantityNotEnoughException(
                    String.format("Your order is %s of Product %s, but there are only %s left",
                            orderedQuantity,
                            product.getName(),
                            product.getQuantity()));
        }
    }
}
