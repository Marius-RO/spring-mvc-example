package com.company.service.impl;

import com.company.dto.ProductDto;
import com.company.model.Product;
import com.company.model.UserActivity;
import com.company.repository.AccountRepository;
import com.company.repository.ProductRepository;
import com.company.service.ProductService;
import com.company.service.impl.util.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class ProductServiceImpl extends AbstractService implements ProductService {

    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    private interface SortingOptions {
        int BY_PRICE_ASCENDING = 0;
        int BY_PRICE_DESCENDING = 1;
        int BY_DATE_ASCENDING = 2;
        int BY_DATE_DESCENDING = 3;
    }

    @Autowired
    public ProductServiceImpl(WebApplicationContext webApplicationContext, ProductRepository productRepository,
                              AccountRepository accountRepository) {
        super(webApplicationContext);
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    protected Class<?> getClassType() {
        return ProductServiceImpl.class;
    }

    @Override
    public Product getProductById(int productId) {
        return productRepository.getProduct(productId);
    }

    @Override
    public int getProductStock(int productId) {
        return productRepository.getProductStock(productId);
    }

    @Override
    public float getProductPrice(int productId) {
        return productRepository.getProductPrice(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public List<Product> filterProducts(int categoryId, String searchValue, float maxPrice, int sortOption) {
         /*
         Priority for filter/sorting is as following:
            1. filter after category id
            2. filter after search value
            3. filter after price range
            4. sort after price/date
          */

        searchValue = searchValue.trim();
        switch (sortOption) {
            case SortingOptions.BY_DATE_ASCENDING:
                return categoryId > 0 ?
                        productRepository.filterProductsAndSortByDateAscending(categoryId, searchValue, maxPrice) :
                        productRepository.filterProductsAndSortByDateAscending(searchValue, maxPrice);
            case SortingOptions.BY_DATE_DESCENDING:
                return categoryId > 0 ?
                        productRepository.filterProductsAndSortByDateDescending(categoryId, searchValue, maxPrice) :
                        productRepository.filterProductsAndSortByDateDescending(searchValue, maxPrice);
            case SortingOptions.BY_PRICE_DESCENDING:
                return categoryId > 0 ?
                        productRepository.filterProductsAndSortByPriceDescending(categoryId, searchValue, maxPrice) :
                        productRepository.filterProductsAndSortByPriceDescending(searchValue, maxPrice);
            case SortingOptions.BY_PRICE_ASCENDING:
            default:
                return categoryId > 0 ?
                        productRepository.filterProductsAndSortByPriceAscending(categoryId, searchValue, maxPrice) :
                        productRepository.filterProductsAndSortByPriceAscending(searchValue, maxPrice);
        }
    }

    @Override
    public List<Product> getLastAddedProducts(int limit) {
        return productRepository.getLastAddedProducts(limit);
    }

    @Override
    public int[] getProductCategoriesByProductId(int productId) {
        return productRepository.getProductCategoriesByProductId(productId);
    }

    @Override
    public int addProductAndGetId(ProductDto productDto, String userEmail) {
        Product product = getBasicConversion(productDto, false);
        product.setAdded(webApplicationContext.getBean(Timestamp.class));

        UserActivity userActivity = webApplicationContext.getBean(UserActivity.class);
        userActivity.setTag(UserActivity.Tags.ADD_PRODUCT);
        userActivity.setAdded(webApplicationContext.getBean(Timestamp.class));
        userActivity.setBefore(Product.toJson(null));
        userActivity.setAfter(Product.toJson(product));
        userActivity.setUserAccount(accountRepository.getUser(userEmail));

        return productRepository.insertProductAndGetId(product, userActivity, getCategoriesIds(productDto.getCategoriesIds()));
    }

    @Override
    public void updateProductById(ProductDto productDto, int productId, String userEmail) {
        Product afterProduct = getBasicConversion(productDto, true);
        afterProduct.setId(productId);

        UserActivity userActivity = webApplicationContext.getBean(UserActivity.class);
        userActivity.setTag(UserActivity.Tags.UPDATE_PRODUCT);
        userActivity.setAdded(webApplicationContext.getBean(Timestamp.class));
        userActivity.setBefore(Product.toJson(getProductById(productId)));
        userActivity.setAfter(Product.toJson(afterProduct));
        userActivity.setUserAccount(accountRepository.getUser(userEmail));

        productRepository.updateProduct(afterProduct, userActivity, getCategoriesIds(productDto.getCategoriesIds()));
    }

    @Override
    public void deleteProductById(int productId, String userEmail) {

        UserActivity userActivity = webApplicationContext.getBean(UserActivity.class);
        userActivity.setTag(UserActivity.Tags.DELETE_PRODUCT);
        userActivity.setAdded(webApplicationContext.getBean(Timestamp.class));
        userActivity.setBefore(Product.toJson(getProductById(productId)));
        userActivity.setAfter(Product.toJson(null));
        userActivity.setUserAccount(accountRepository.getUser(userEmail));

        productRepository.deleteProduct(productId, userActivity);
    }

    @Override
    public boolean isAddedImageFileValid(MultipartFile uploadedFile) {
        if(uploadedFile == null){
            // if uploadedFile is null does not matter, because no data exists
            return true;
        }

        if(uploadedFile.getContentType() == null){
            return false;
        }

        return uploadedFile.getContentType().startsWith("image/");
    }

    private Product getBasicConversion(ProductDto productDto, boolean forUpdate){
        Product product = webApplicationContext.getBean(Product.class);
        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice().floatValue());
        product.setStock(productDto.getStock());
        product.setImageBase64(productDto.getImageBase64());

        MultipartFile multipartFile = productDto.getImageMultipartFile();
        if(multipartFile != null){
            final boolean isForUpdateAndNewValidFileImageWasUploaded = forUpdate &&
                    multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("image/");

            // when product is added check is made when file is uploaded using 'isAddedImageFileValid(...)' method, so
            // no check is required here
            if(!forUpdate || isForUpdateAndNewValidFileImageWasUploaded){
                try {
                    product.setImageBase64(Base64Utils.encodeToString(multipartFile.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return product;
    }

    private HashSet<Integer> getCategoriesIds(int[] categoryIds) {
        HashSet<Integer> ids = new HashSet<>();
        Arrays.stream(categoryIds).forEach(ids::add);
        return ids;
    }
}
