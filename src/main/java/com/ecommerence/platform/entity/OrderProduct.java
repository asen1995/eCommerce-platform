package com.ecommerence.platform.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "orders_products")
@Data
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    @SequenceGenerator(name = "order_id_seq", sequenceName = "order_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_quantity")
    private Integer quantity;

}
