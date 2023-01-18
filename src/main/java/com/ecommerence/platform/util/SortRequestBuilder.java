package com.ecommerence.platform.util;

import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import org.springframework.data.domain.Sort;

public class SortRequestBuilder {

    public static Sort build(ProductOrderEnum orderBy, DirectionEnum direction) {

        switch (direction) {
            case DESC:
                return Sort.by(orderBy.getValue()).descending();
            default:
                return Sort.by(orderBy.getValue()).ascending();
        }
    }
}
