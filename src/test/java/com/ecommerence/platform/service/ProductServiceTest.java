package com.ecommerence.platform.service;

import com.ecommerence.platform.dto.ProductDto;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.ProductsResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@WebMvcTest(ProductService.class)
@ActiveProfiles("test")
public class ProductServiceTest {
    @Autowired
    private IProductService productService;

    @MockBean
    private ProductRepository productRepository;

    private Product product;

    @Before
    public void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Dell monitor");
        product.setCategory("Monitor");
        product.setQuantity(10);
        product.setDescription("Monitor for testing");
    }


    @Test
    public void testGetProducts_twoPerPage() {

        final int pageSize = 2;
        final int page = 0;
        final DirectionEnum direction = DirectionEnum.ASC;
        final ProductOrderEnum column = ProductOrderEnum.NAME;
        Page<Product> currentPageProducts = new PageImpl<>(Arrays.asList(product, product));

        when(productRepository.findAll(any(Pageable.class))).thenReturn(currentPageProducts);

        ProductsResponse productsResponse = productService.getProducts(column, direction, page, pageSize);

        assertEquals(2, productsResponse.getProducts().size());
    }

    @Test
    public void testGetProducts_DescByName() {

        final int pageSize = 2;
        final int page = 0;
        final DirectionEnum direction = DirectionEnum.DESC;
        final ProductOrderEnum column = ProductOrderEnum.NAME;

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Dell monitor");
        product1.setCategory("Monitor");
        product1.setQuantity(10);
        product1.setDescription("Monitor for testing");


        Product product2 = new Product();
        product2.setId(2);
        product2.setName("HP monitor");
        product2.setCategory("Monitor");
        product2.setQuantity(10);
        product2.setDescription("Monitor for testing");

        Page<Product> currentPageProducts = new PageImpl<>(Arrays.asList(product2, product1));

        when(productRepository.findAll(any(Pageable.class))).thenReturn(currentPageProducts);

        ProductsResponse productsResponse = productService.getProducts(column, direction, page, pageSize);

        assertEquals("HP monitor", productsResponse.getProducts().get(0).getName());
    }

    @Test
    public void testCreateProduct() {

        ProductDto productDto = new ProductDto();
        productDto.setName("Dell monitor");
        productDto.setCategory("Monitor");
        productDto.setQuantity(10);
        productDto.setDescription("Monitor for testing");

        ProductDto resultProduct = productService.createProduct(productDto);
        assertEquals(productDto, resultProduct);
    }


    @Test
    public void testDeleteProduct() throws ProductNotFoundException {
        when(productRepository.existsById(anyInt())).thenReturn(true);

        productService.deleteProduct(1);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteNotExistingProduct() throws ProductNotFoundException {
        when(productRepository.existsById(anyInt())).thenReturn(false);

        productService.deleteProduct(1);
    }

    @Test
    public void testUpdateProduct() throws ProductNotFoundException {

        ProductDto productDto = new ProductDto();
        productDto.setName("Dell monitor");
        productDto.setCategory("Monitor");
        productDto.setQuantity(10);
        productDto.setDescription("Monitor for testing");

        when(productRepository.findById(anyInt())).thenReturn(Optional.ofNullable(product));
        ProductDto result = productService.updateProduct(1, productDto);
        assertEquals(productDto, result);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testUpdateNonExistingProduct() throws ProductNotFoundException {

        ProductDto productDto = new ProductDto();
        productDto.setName("Dell monitor");
        productDto.setCategory("Monitor");
        productDto.setQuantity(10);
        productDto.setDescription("Monitor for testing");

        when(productRepository.existsById(anyInt())).thenReturn(false);
        ProductDto result = productService.updateProduct(1, productDto);
        assertEquals(product, result);
    }
}
