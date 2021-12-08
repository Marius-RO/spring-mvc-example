package com.company.repository.impl;

import com.company.model.Category;
import com.company.model.Product;
import com.company.model.UserActivity;
import com.company.repository.ProductRepository;
import com.company.repository.impl.util.AbstractRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ProductRepositoryImpl extends AbstractRepository implements ProductRepository {

    private static final String DEF_GET_ALL_PRODUCTS_HQL = "SELECT p FROM Product p";
    private static final String DEF_GET_PRODUCT_BU_ID_WITH_CATEGORIES_HQL = "SELECT p " +
                                                                            "FROM Product p JOIN FETCH p.categoryList " +
                                                                            "WHERE p.id = ?1";
    private static final String DEF_GET_LAST_ADDED_PRODUCTS_WITH_LIMIT_HQL = "SELECT p FROM Product p ORDER BY p.id DESC";

    // for filter and sorting
    private static final String DEF_BASE_DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_HQL =
            "SELECT p " +
            "FROM Product p JOIN FETCH p.categoryList category " +
            "WHERE category.id = ?1 AND " +
            "p.name LIKE ?2 AND " +
            "p.price BETWEEN 0 AND ?3 " +
            "ORDER BY";

    private static final String DEF_BASE_DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_HQL = "SELECT p " +
                                                                                      "FROM Product p " +
                                                                                      "WHERE p.name LIKE ?1 AND " +
                                                                                      "p.price BETWEEN 0 AND ?2 " +
                                                                                      "ORDER BY ";

    private static final String DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_PRICE_ASCENDING_HQL =
            DEF_BASE_DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_HQL + " p.price ASC";

    private static final String DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_PRICE_ASCENDING_HQL =
            DEF_BASE_DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_HQL + " price ASC";

    private static final String DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_PRICE_DESCENDING_HQL =
            DEF_BASE_DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_HQL + " p.price DESC";

    private static final String DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_PRICE_DESCENDING_HQL =
            DEF_BASE_DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_HQL + " price DESC";

    private static final String DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_DATE_ASCENDING_HQL =
            DEF_BASE_DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_HQL + " p.added ASC";

    private static final String DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_DATE_ASCENDING_HQL =
            DEF_BASE_DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_HQL + " added ASC";

    private static final String DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_DATE_DESCENDING_HQL =
            DEF_BASE_DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_HQL + " p.added DESC";

    private static final String DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_DATE_DESCENDING_HQL =
            DEF_BASE_DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_HQL + " added DESC";

    @Autowired
    public ProductRepositoryImpl(WebApplicationContext webApplicationContext, LocalSessionFactoryBean sessionFactory) {
        super(webApplicationContext, sessionFactory);
    }

    @Override
    protected Class<?> getClassType() {
        return ProductRepositoryImpl.class;
    }

    @Override
    public Product getProduct(int productId) {
        return super.executeFetchOperation(session -> session.get(Product.class, productId));
    }

    @Override
    public Product getProductWithCategories(int productId) {
        return super.executeFetchOperation(session -> executeGetProductWithCategories(session, productId));
    }

    private Product executeGetProductWithCategories(Session session, int productId){
        return session
                .createQuery(DEF_GET_PRODUCT_BU_ID_WITH_CATEGORIES_HQL, Product.class)
                .setParameter(1, productId)
                .getSingleResult();
    }

    @Override
    public int getProductStock(int productId) {
        return getProduct(productId).getStock();
    }

    @Override
    public float getProductPrice(int productId) {
        return getProduct(productId).getPrice();
    }

    @Override
    public List<Product> getAllProducts() {
        return super.executeFetchOperation(this::executeGetAllProducts);
    }

    private List<Product> executeGetAllProducts(Session session){
        return session
                .createQuery(DEF_GET_ALL_PRODUCTS_HQL, Product.class)
                .list();
    }

    @Override
    public List<Product> getLastAddedProducts(int limit) {
        return super.executeFetchOperation(session -> executeGetLastAddedProducts(session, limit));
    }

    private List<Product> executeGetLastAddedProducts(Session session, int limit){
        return session
                .createQuery(DEF_GET_LAST_ADDED_PRODUCTS_WITH_LIMIT_HQL, Product.class)
                .setMaxResults(limit)
                .list();
    }

    @Override
    public int[] getProductCategoriesByProductId(int productId) {
        return super.executeFetchOperation(session -> executeGetProductCategoriesByProductId(session, productId));
    }

    private int[] executeGetProductCategoriesByProductId(Session session, int productId){
        Product product = session.get(Product.class, productId);
        if(product == null){
            return new int[0];
        }

        Hibernate.initialize(product.getCategoryList());

        if(product.getCategoryList() == null){
            return new int[0];
        }

        return product.getCategoryList()
                 .stream()
                 .mapToInt(Category::getId)
                 .toArray();
    }

    @Override
    public int insertProductAndGetId(Product product, UserActivity userActivity, HashSet<Integer> categoriesIds) {
        AtomicInteger insertedProductId = new AtomicInteger();
        super.executeInsertOperation(session ->
                executeInsertProductAndGetId(session, product, userActivity, categoriesIds, insertedProductId));
        return insertedProductId.get();
    }

    private void executeInsertProductAndGetId(Session session, Product product, UserActivity userActivity,
                                              HashSet<Integer> categoriesIds, AtomicInteger insertedProductId ){
        session.save(userActivity);
        insertedProductId.set((Integer) session.save(product));
        categoriesIds.forEach(id -> addProductIntoCategory(session, id, product));
    }

    private void addProductIntoCategory(Session session, int categoryId, Product product){
        Optional.ofNullable(session.get(Category.class, categoryId))
                .ifPresent(category -> Optional.ofNullable(category.getProductList())
                                                .ifPresent(productList -> {
                                                    productList.add(product);
                                                    session.update(category);
                                                })
                );
    }

    private List<Category> getProductCategories(Session session, HashSet<Integer> categoriesIds){
        List<Category> productCategories = new ArrayList<>();
        categoriesIds.forEach(id -> {
            Category tmp = session.get(Category.class, id);
            if(tmp != null){
                productCategories.add(tmp);
            }
        });
        return productCategories;
    }

    @Override
    public void updateProduct(Product product, UserActivity userActivity, HashSet<Integer> categoriesIds) {
        super.executeUpdateOperation(session -> executeUpdateProduct(session, product, userActivity, categoriesIds));
    }

    private void executeUpdateProduct(Session session, Product product, UserActivity userActivity,
                                      HashSet<Integer> categoriesIds){
        session.save(userActivity);

        Product oldProduct = session.get(Product.class, product.getId());

        // delete product from previous categories
        oldProduct.getCategoryList().forEach(category -> {
            category.getProductList().remove(oldProduct);
            session.update(category);
        });

        session.detach(oldProduct);

        // add product to the new categories
        getProductCategories(session, categoriesIds).forEach(category -> {
            category.getProductList().add(product);
            session.update(category);
        });

        session.update(product);
    }

    @Override
    public void deleteProduct(int productId, UserActivity userActivity) {
        super.executeDeleteOperation(session -> executeDeleteProduct(session, productId, userActivity));
    }

    private void executeDeleteProduct(Session session, int productId, UserActivity userActivity){
        Product product = session.get(Product.class, productId);
        if(product == null){
            return;
        }

        // first delete product from each category because product is not the ownership of the 'many-to-many' relationship
        // https://stackoverflow.com/questions/1082095/how-to-remove-entity-with-manytomany-relationship-in-jpa-and-corresponding-join
        List<Category> categoryList = product.getCategoryList();
        if(categoryList != null){
            categoryList.forEach(category -> {
                List<Product> tmp = category.getProductList();
                if(tmp != null){
                    tmp.remove(product);
                    session.update(category);
                }
            });
        }

        session.remove(product);
        session.save(userActivity);
    }

    @Override
    public List<Product> filterProductsAndSortByDateAscending(int categoryId, String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_DATE_ASCENDING_HQL,
                getFilterArgs(categoryId, searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByDateAscending(String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_DATE_ASCENDING_HQL,
                getFilterArgs(searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByDateDescending(int categoryId, String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_DATE_DESCENDING_HQL,
                getFilterArgs(categoryId, searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByDateDescending(String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_DATE_DESCENDING_HQL,
                getFilterArgs(searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByPriceAscending(int categoryId, String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_PRICE_ASCENDING_HQL,
                getFilterArgs(categoryId, searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByPriceAscending(String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_PRICE_ASCENDING_HQL,
                getFilterArgs(searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByPriceDescending(int categoryId, String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_PRICE_DESCENDING_HQL,
                getFilterArgs(categoryId, searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByPriceDescending(String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_PRICE_DESCENDING_HQL,
                getFilterArgs(searchValue, maxPrice)
        );
    }


    private List<Product> filterAndSort(String sql, Object[] args){
        return super.executeFetchOperation(session -> executeFilterAndSort(session, sql, args));
    }

    private List<Product> executeFilterAndSort(Session session, String sql, Object[] args){
        Query<Product> query = session.createQuery(sql, Product.class);
        for(int i = 0; i < args.length; i++){
            query.setParameter(i + 1, args[i]);
        }
        return query.list();
    }


    private Object[] getFilterArgs(int categoryId, String searchValue, float maxPrice){
        return new Object[]{categoryId, "%" + searchValue + "%", maxPrice};
    }

    private Object[] getFilterArgs(String searchValue, float maxPrice){
        return new Object[]{"%" + searchValue + "%", maxPrice};
    }
}
