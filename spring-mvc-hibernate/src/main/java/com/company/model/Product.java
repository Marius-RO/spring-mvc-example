package com.company.model;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Lob
    @Column(name = "image_base_64")
    private String imageBase64;

    @Column(name = "added", nullable = false)
    private Timestamp added;

    @Transient
    private boolean isAddedToCart;

    @ToString.Exclude
    @ManyToMany(mappedBy = "productList")
    private List<Category> categoryList;

    public static String toJson(Product product){
        Gson gson = new Gson();

        if(product == null){
            return gson.toJson("{}");
        }

        if(product.getCategoryList() != null){
            product.getCategoryList().forEach(category -> {
                if(category != null) {
                    // avoid circular lazy initialize
                    category.setProductList(null);
                }
            });
        }

        return gson.toJson(product, Product.class);
    }

    @Nullable
    public static Product fromJson(String json){
        if(json == null){
            return null;
        }

        try{
            return new Gson().fromJson(json, Product.class);
        }
        catch (JsonParseException ex){
            return null;
        }
    }
}
