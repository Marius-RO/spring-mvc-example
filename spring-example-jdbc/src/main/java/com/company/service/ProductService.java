package com.company.service;

import com.company.dto.ProductDto;
import com.company.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product getProductById(int productId);
    int getProductStock(int productId);
    float getProductPrice(int productId);
    List<Product> getAllProducts();
    List<Product> getLastAddedProducts(int limit);
    int[] getProductCategoriesByProductId(int productId);
    int addProductAndGetId(ProductDto productDto, String userEmail);
    void updateProductById(ProductDto productDto, int productId, String userEmail);
    void deleteProductById(int productId, String userEmail);
    boolean isAddedImageFileValid(MultipartFile uploadedFile);
}
