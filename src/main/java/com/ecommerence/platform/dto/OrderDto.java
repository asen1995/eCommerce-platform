package com.ecommerence.platform.dto;

import com.ecommerence.platform.constants.AppConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class OrderDto {

    @NotBlank(message = AppConstants.ORDER_NAME_IS_MANDATORY_MESSAGE)
    @Size(min = 3, max = 50, message = AppConstants.ORDER_NAME_SIZE_VALIDATION_ERROR_MESSAGE)
    private String name;

    @NotBlank(message = AppConstants.ORDER_COMMENT_IS_MANDATORY_MESSAGE)
    @Size(min = 20, max = 250, message = AppConstants.ORDER_COMMENT_SIZE_VALIDATION_ERROR_MESSAGE)
    private String comment;

    @NotEmpty(message = AppConstants.ORDER_MUST_CONTAIN_AT_LEAST_ONE_PRODUCT_MESSAGE)
    private List<ProductQuantityPairDto> productQuantityPairDtoList;

}
