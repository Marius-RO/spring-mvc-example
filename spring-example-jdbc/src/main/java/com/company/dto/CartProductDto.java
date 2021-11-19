package com.company.dto;

import com.company.util.GeneralUtilities;
import com.company.util.SessionCart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@Getter
@Setter
@ToString
public class CartProductDto {

    private int productId;
    private String productName;
    private int quantity;
    private double productPrice;
    private double totalPrice;

    private boolean outOfStock;
    private boolean tooMuchQuantity;
    private int currentStock;

    public void setAvailabilityStatus(int currentStock){
        if(currentStock <= 0){
            outOfStock = true;
            tooMuchQuantity = false;
            return;
        }

        if(quantity > currentStock){
            outOfStock = false;
            tooMuchQuantity = true;
            this.currentStock = currentStock;
            return;
        }

        outOfStock = false;
        tooMuchQuantity = false;
    }

    public void setQuantity(int quantity) {
        if(quantity > SessionCart.MAX_ITEMS_PER_TYPE){
            return;
        }
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return GeneralUtilities.getDoubleFormatted(totalPrice, "######.##");
    }

    public double getProductPrice() {
        return GeneralUtilities.getDoubleFormatted(productPrice, "######.##");
    }

    @NonNull @NotNull
    public static String fromHashSetToJson(HashSet<CartProductDto> values) {
        if (values == null) {
            values = new LinkedHashSet<>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedHashSet<CartProductDto>>() {}.getType();
        String conversion = gson.toJson(values, type);
        return conversion == null ? gson.toJson("") : conversion;
    }

    @NonNull @NotNull
    public static HashSet<CartProductDto> fromJsonToHashSet(String value) {
        if (value== null) {
            return new LinkedHashSet<>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartProductDto>>() {}.getType();
        ArrayList<CartProductDto> conversion = gson.fromJson(value, type);
        if(conversion == null){
            return new LinkedHashSet<>();
        }
        return new LinkedHashSet<>(conversion);
    }
}
