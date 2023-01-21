package com.ecommerence.platform.service;

import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.exception.ProductQuantityNotEnoughException;
import com.ecommerence.platform.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderService {

    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Transactional(isolation = Isolation.READ_COMMITTED) //SERIALIZABLE causes the second waiting transaction to instant fail in Oracle db once the first transaction commit
    public String orderProduct(Integer id, Integer orderedQuantity) throws Exception {

        Optional<Product> oProduct = productRepository.findByIdForUpdate(id);

        if (!oProduct.isPresent()) {
            throw new ProductNotFoundException("Product not found");
        }

        Product product = oProduct.get();

        if (product.getQuantity() >= orderedQuantity) {
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
}
