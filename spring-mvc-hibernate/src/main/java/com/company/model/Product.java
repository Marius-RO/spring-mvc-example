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

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Product {
    private int id;
    private int stock;
    private float price;
    private String name;
    private String description;
    private String imageBase64;
    private Timestamp added;

    // not used in DB
    private boolean isAddedToCart;

    public static String toJson(Product product){
        Gson gson = new Gson();
        if(product == null){
            return gson.toJson("{}");
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
