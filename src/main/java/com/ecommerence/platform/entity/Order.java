package com.ecommerence.platform.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@ToString(exclude = {"orderProducts", "customer"})
@EqualsAndHashCode(exclude = {"orderProducts", "customer"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    @SequenceGenerator(name = "order_id_seq", sequenceName = "order_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "order_name")
    private String name;

    @Column(name = "order_comment")
    private String comment;


    @OneToMany(mappedBy = "order")
    @JsonManagedReference
    private List<OrderProduct> orderProducts;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Column(name = "order_approved")
    private Boolean approved;
}
