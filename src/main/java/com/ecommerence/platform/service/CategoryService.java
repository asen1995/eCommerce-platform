package com.ecommerence.platform.service;

import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.model.Category;
import com.ecommerence.platform.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    ProductRepository productRepository;

    public CategoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

        for (Map.Entry<String, Integer> entry : categoryNumberOfProductsMap.entrySet()) {
            result.add(new Category(entry.getKey(), entry.getValue()));
        }

        return result;
    }


}
