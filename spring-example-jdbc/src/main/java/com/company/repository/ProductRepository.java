package com.company.repository;

import com.company.model.Product;
import com.company.model.UserActivity;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.List;

public interface ProductRepository {
    Product getProduct(int productId);
    int getProductStock(int productId);
    float getProductPrice(int productId);
    List<Product> getAllProducts();
    List<Product> getLastAddedProducts(int limit);
    int[] getProductCategoriesByProductId(int productId);
    int insertProductAndGetId(Product product, UserActivity userActivity, HashSet<Integer> categoriesIds);
    void updateProduct(Product product, UserActivity userActivity, HashSet<Integer> categoriesIds);
    void deleteProduct(int productId, UserActivity userActivity);
}
