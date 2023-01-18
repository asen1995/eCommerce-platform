package com.ecommerence.platform.service;

import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.exception.ProductQuantityNotEnoughException;
import com.ecommerence.platform.model.Category;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.ProductsResponse;
import com.ecommerence.platform.util.SortRequestBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Transactional(isolation = Isolation.READ_COMMITTED) //SERIALIZABLE causes the second waiting transaction to instant fail in Oracle db once the first transaction commit
    public String orderProduct(Integer id, Integer orderedQuantity) throws Exception {

        Optional<Product> oProduct = productRepository.findByIdForUpdate(id);

        if (!oProduct.isPresent()) {
            throw new ProductNotFoundException("Product not found");
        }

        Product product = oProduct.get();

        if (product.getQuantity() > orderedQuantity) {
            product.setQuantity(product.getQuantity() - orderedQuantity);
            productRepository.save(product);

            System.out.println("you ordered " + orderedQuantity + " " + product.getName());
            return String.format("You successfully ordered %s %s", orderedQuantity, product.getName());
        } else {
            System.out.println("Not enough quantity");
            throw new ProductQuantityNotEnoughException(
                    String.format("Your order is %s of Product %s, but there are only %s left",
                            orderedQuantity,
                            product.getName(),
                            product.getQuantity()));
        }
    }

    public List<Category> getProductsAvailablePerCategories() {

        List<Product> all = productRepository.findAll();

        Map<String, Integer> categoryNumberOfProductsMap = new HashMap<>();

        for (Product product : all) {

            if (categoryNumberOfProductsMap.containsKey(product.getCategory())) {
                categoryNumberOfProductsMap.put(product.getCategory(),
                        (categoryNumberOfProductsMap.get(product.getCategory()) + product.getQuantity()));
            } else {
                categoryNumberOfProductsMap.put(product.getCategory(), product.getQuantity());
            }

        }

        List<Category> result = new ArrayList<>(categoryNumberOfProductsMap.size());

        for(Map.Entry<String, Integer> entry : categoryNumberOfProductsMap.entrySet()) {
            result.add(new Category(entry.getKey(), entry.getValue()));
        }

        return result;
    }
}
