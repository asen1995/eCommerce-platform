package com.ecommerence.platform.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_category")
    private String category;

    @Column(name = "product_description")
    private String description;

    @Column(name = "product_quantity")
    private Integer quantity;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<OrderProduct> orderProducts;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date")
    private Date updatedDate;
}
