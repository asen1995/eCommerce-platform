package com.ecommerence.platform.entity;

import com.ecommerence.platform.constants.AppConstants;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_name", "product_category"})
})
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", allocationSize = 1)
    private Integer id;

    @NotBlank(message = AppConstants.PRODUCT_NAME_IS_MANDATORY_MESSAGE)
    @Size(min = 3, max = 50, message = AppConstants.PRODUCT_NAME_SIZE_VALIDATION_ERROR_MESSAGE)
    @Column(name = "product_name")
    private String name;

    @NotBlank(message = AppConstants.PRODUCT_CATEGORY_IS_MANDATORY_MESSAGE)
    @Size(min = 3, max = 30, message = AppConstants.PRODUCT_CATEGORY_SIZE_VALIDATION_ERROR_MESSAGE)
    @Column(name = "product_category")
    private String category;

    @Size(max = 250, message = AppConstants.PRODUCT_DESCRIPTION_SIZE_VALIDATION_ERROR_MESSAGE)
    @Column(name = "product_description")
    private String description;

    @NotNull(message = AppConstants.PRODUCT_QUANTITY_IS_MANDATORY_MESSAGE)
    @Min(value = 0, message = AppConstants.QTY_MUST_BE_GREATER_THAN_ZERO_MESSAGE)
    @Column(name = "product_quantity")
    private Integer quantity;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date")
    private Date updatedDate;
}
