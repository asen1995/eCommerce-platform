package com.ecommerence.platform.entity;

import com.ecommerence.platform.constants.AppConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"customer_username"})
        })
@Data
@ToString(exclude = {"orders"})
@EqualsAndHashCode(exclude = {"orders"})
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_id_seq", allocationSize = 1)
    private Integer id;

    @NotBlank(message = AppConstants.CUSTOMER_NAME_IS_MANDATORY_MESSAGE)
    @Column(name = "customer_username")
    private String username;

    @NotBlank(message = AppConstants.CUSTOMER_PASSWORD_IS_MANDATORY_MESSAGE)
    @Column(name = "customer_password")
    private String password;

    @Column(name = "customer_first_name")
    private String firstName;

    @Column(name = "customer_last_name")
    private String lastName;

    @Column(name = "customer_email")
    private String email;

    @Column(name = "customer_phone")
    private String phone;

    @Column(name = "customer_address")
    private String address;

    @Column(name = "customer_city")
    private String city;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;
}
