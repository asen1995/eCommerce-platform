package com.ecommerence.platform.dto;

import com.ecommerence.platform.constants.AppConstants;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProductDto {

    @NotBlank(message = AppConstants.PRODUCT_NAME_IS_MANDATORY_MESSAGE)
    @Size(min = 3, max = 50, message = AppConstants.PRODUCT_NAME_SIZE_VALIDATION_ERROR_MESSAGE)
    private String name;

    @NotBlank(message = AppConstants.PRODUCT_CATEGORY_IS_MANDATORY_MESSAGE)
    @Size(min = 3, max = 30, message = AppConstants.PRODUCT_CATEGORY_SIZE_VALIDATION_ERROR_MESSAGE)
    private String category;

    @Size(max = 250, message = AppConstants.PRODUCT_DESCRIPTION_SIZE_VALIDATION_ERROR_MESSAGE)
    private String description;

    @NotNull(message = AppConstants.PRODUCT_QUANTITY_IS_MANDATORY_MESSAGE)
    @Min(value = 0, message = AppConstants.QTY_MUST_BE_GREATER_THAN_ZERO_MESSAGE)
    private Integer quantity;

}
