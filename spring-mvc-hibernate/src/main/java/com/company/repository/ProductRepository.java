package com.company.repository;

import com.company.model.Product;
import com.company.model.UserActivity;

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
    List<Product> filterProductsAndSortByDateAscending(int categoryId, String searchValue, float maxPrice);
    List<Product> filterProductsAndSortByDateAscending(String searchValue, float maxPrice);
    List<Product> filterProductsAndSortByDateDescending(int categoryId, String searchValue, float maxPrice);
    List<Product> filterProductsAndSortByDateDescending(String searchValue, float maxPrice);
    List<Product> filterProductsAndSortByPriceAscending(int categoryId, String searchValue, float maxPrice);
    List<Product> filterProductsAndSortByPriceAscending(String searchValue, float maxPrice);
    List<Product> filterProductsAndSortByPriceDescending(int categoryId, String searchValue, float maxPrice);
    List<Product> filterProductsAndSortByPriceDescending(String searchValue, float maxPrice);
}
