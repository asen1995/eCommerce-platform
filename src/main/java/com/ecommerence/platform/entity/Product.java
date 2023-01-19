package com.ecommerence.platform.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank(message = "Name is mandatory")
    @Column(name = "product_name")
    private String name;

    @NotBlank(message = "Category is mandatory")
    @Column(name = "product_category")
    private String category;

    @Column(name = "product_description")
    private String description;

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 1, message = "Quantity must be greater or equal to 1")
    @Column(name = "product_quantity")
    private Integer quantity;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date")
    private Date updatedDate;
}
