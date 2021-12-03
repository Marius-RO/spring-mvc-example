package com.company.model;

import com.company.dto.CartProductDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashSet;

@Getter
@Setter
@ToString
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Order {
    private int id;
    private Timestamp added;
    private double amount;
    private String userEmail;
    private String fullName;
    private String fullAddress;
    private String productsJson;

    // not linked in DB
    private HashSet<CartProductDto> productsList;
}
