package com.company.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "category_product_many_to_many",
            joinColumns = { @JoinColumn(name = "fk_category_id") },
            inverseJoinColumns = { @JoinColumn(name = "fk_product_id") }
    )
    private List<Product> productList;
}
