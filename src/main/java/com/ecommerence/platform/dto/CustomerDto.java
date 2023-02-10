package com.ecommerence.platform.dto;

import com.ecommerence.platform.constants.AppConstants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CustomerDto {

    @NotBlank(message = AppConstants.USERNAME_IS_MANDATORY_MESSAGE)
    @Size(min = 3, max = 50, message = AppConstants.USERNAME_SIZE_VALIDATION_ERROR_MESSAGE)
    private String username;

    @NotBlank(message = AppConstants.PASSWORD_IS_MANDATORY_MESSAGE)
    @Size(min = 3, max = 50, message = AppConstants.PASSWORD_SIZE_VALIDATION_ERROR_MESSAGE)
    private String password;

    @NotBlank(message = AppConstants.FIRST_NAME_IS_MANDATORY_MESSAGE)
    @Size(min = 2, max = 50, message = AppConstants.FIRST_NAME_SIZE_VALIDATION_ERROR_MESSAGE)
    private String firstName;

    @NotBlank(message = AppConstants.LAST_NAME_IS_MANDATORY_MESSAGE)
    @Size(min = 2, max = 50, message = AppConstants.LAST_NAME_SIZE_VALIDATION_ERROR_MESSAGE)
    private String lastName;

    @Email(message = AppConstants.EMAIL_VALIDATION_FORMAT_ERROR_MESSAGE)
    private String email;

    private String phone;
    private String address;
    private String city;
}
