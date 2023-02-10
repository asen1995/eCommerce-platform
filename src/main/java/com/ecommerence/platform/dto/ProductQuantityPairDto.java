package com.ecommerence.platform.dto;

import com.ecommerence.platform.constants.AppConstants;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProductQuantityPairDto {

    @NotNull(message = AppConstants.PRODUCT_ID_IS_MANDATORY_MESSAGE)
    private Integer productId;

    @NotNull(message = AppConstants.PRODUCT_QUANTITY_IS_MANDATORY_MESSAGE)
    @Min(value = 0, message = AppConstants.QTY_MUST_BE_GREATER_THAN_ZERO_MESSAGE)
    private Integer quantity;
}
