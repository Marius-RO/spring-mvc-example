package com.company.model;

import com.company.dto.CartProductDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;

@Getter
@Setter
@ToString
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "added", nullable = false)
    private Timestamp added;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "email", nullable = false)
    private String userEmail;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "full_address", nullable = false, length = 512)
    private String fullAddress;

    @Lob
    @Column(name = "products_json", nullable = false)
    private String productsJson;

    @Transient
    private HashSet<CartProductDto> productsList;
}
