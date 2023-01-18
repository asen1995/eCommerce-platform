package com.ecommerence.platform.util;

import org.junit.Test;
import org.springframework.data.domain.Sort;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;

import static org.junit.Assert.assertEquals;

public class SortRequestBuilderTest {

    @Test
    public void testWhenDirectionIsDesc() {
        Sort sort = SortRequestBuilder.build(ProductOrderEnum.NAME, DirectionEnum.DESC);
        assertEquals("name: DESC", sort.toString());
    }

    @Test
    public void testWhenDirectionIsAsc() {
        Sort sort = SortRequestBuilder.build(ProductOrderEnum.NAME, DirectionEnum.ASC);
        assertEquals("name: ASC", sort.toString());
    }
}
