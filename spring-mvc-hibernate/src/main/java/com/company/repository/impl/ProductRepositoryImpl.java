package com.company.repository.impl;

import com.company.model.Product;
import com.company.model.UserActivity;
import com.company.repository.ProductRepository;
import com.company.repository.impl.util.AbstractRepository;
import com.company.repository.mappers.product.ProductCategoriesByProductIdResultSetExtractor;
import com.company.repository.mappers.product.ProductRowMapper;
import com.company.repository.mappers.product.ProductsResultSetExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class ProductRepositoryImpl extends AbstractRepository implements ProductRepository {

    private static final String DEF_GET_ALL_PRODUCTS_SQL = "SELECT * FROM products";
    private static final String DEF_GET_LAST_ADDED_PRODUCTS_WITH_LIMIT_SQL = "SELECT * FROM products ORDER BY id DESC limit ?";
    private static final String DEF_GET_PRODUCT_SQL = "SELECT * FROM products WHERE id = ?";

    private static final String DEF_GET_PRODUCT_CATEGORIES_IDS_BY_PRODUCT_ID_SQL =
            "SELECT DISTINCT fk_category_id FROM product_category_many_to_many WHERE fk_product_id = ?";

    private static final String DEF_INSERT_PRODUCT_SQL =
            "INSERT INTO products (price, stock, name, description, image_base_64, added) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String DEF_UPDATE_PRODUCT_SQL =
         "UPDATE products " +
         "SET price = ?, stock = ?, name = ?, description = ?, image_base_64 = ?, added = ? " +
         "WHERE id = ?";

    private static final String DEF_DELETE_PRODUCT_SQL = "DELETE FROM products WHERE id = ?";

    private static final String DEF_INSERT_PRODUCT_RELATED_ACTIVITY_SQL =
            "INSERT INTO user_activities (tag, before_value, after_value, added, fk_username) VALUES (?, ?, ?, ?, ?)";

    private static final String DEF_INSERT_PRODUCT_CATEGORY_RELATION_MANY_TO_MANY_SQL =
            "INSERT INTO product_category_many_to_many (fk_product_id, fk_category_id) VALUES (?, ?)";

    private static final String DEF_DELETE_PRODUCT_CATEGORY_RELATION_MANY_TO_MANY_SQL =
            "DELETE FROM product_category_many_to_many WHERE fk_product_id = ?";

    // for filter and sorting
    private static final String DEF_BASE_DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_SQL =
            "SELECT id, price, stock, name, description, image_base_64, added " +
            "FROM products p JOIN product_category_many_to_many pcm ON (p.id = pcm.fk_product_id) " +
            "WHERE pcm.fk_category_id = ? AND " +
            "p.name LIKE ? AND " +
            "p.price BETWEEN 0 AND ? " +
            "ORDER BY";

    private static final String DEF_BASE_DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_SQL = "SELECT * " +
                                                                                      "FROM products " +
                                                                                      "WHERE name LIKE ? AND " +
                                                                                      "price BETWEEN 0 AND ? " +
                                                                                      "ORDER BY ";

    private static final String DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_PRICE_ASCENDING_SQL =
            DEF_BASE_DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_SQL + " p.price ASC";

    private static final String DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_PRICE_ASCENDING_SQL =
            DEF_BASE_DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_SQL + " price ASC";

    private static final String DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_PRICE_DESCENDING_SQL =
            DEF_BASE_DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_SQL + " p.price DESC";

    private static final String DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_PRICE_DESCENDING_SQL =
            DEF_BASE_DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_SQL + " price DESC";

    private static final String DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_DATE_ASCENDING_SQL =
            DEF_BASE_DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_SQL + " p.added ASC";

    private static final String DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_DATE_ASCENDING_SQL =
            DEF_BASE_DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_SQL + " added ASC";

    private static final String DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_DATE_DESCENDING_SQL =
            DEF_BASE_DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_SQL + " p.added DESC";

    private static final String DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_DATE_DESCENDING_SQL =
            DEF_BASE_DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_SQL + " added DESC";




    @Autowired
    public ProductRepositoryImpl(WebApplicationContext webApplicationContext, JdbcTemplate jdbcTemplate) {
        super(webApplicationContext, jdbcTemplate);
    }

    @Override
    protected Class<?> getClassType() {
        return ProductRepositoryImpl.class;
    }

    @Override
    public Product getProduct(int productId) {
        try{
            Object[] args = {productId};
            return jdbcTemplate.queryForObject(DEF_GET_PRODUCT_SQL, args, webApplicationContext.getBean(ProductRowMapper.class));
        }
        catch (DataAccessException ex){
            return webApplicationContext.getBean(Product.class);
        }
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
        return jdbcTemplate.query(DEF_GET_ALL_PRODUCTS_SQL, webApplicationContext.getBean(ProductsResultSetExtractor.class));
    }

    @Override
    public List<Product> getLastAddedProducts(int limit) {
        return jdbcTemplate.query(DEF_GET_LAST_ADDED_PRODUCTS_WITH_LIMIT_SQL, new Object[]{limit},
                webApplicationContext.getBean(ProductsResultSetExtractor.class));
    }

    @Override
    public int[] getProductCategoriesByProductId(int productId) {
        List<Integer> ids = jdbcTemplate.query(
                DEF_GET_PRODUCT_CATEGORIES_IDS_BY_PRODUCT_ID_SQL,
                new Object[]{productId},
                webApplicationContext.getBean(ProductCategoriesByProductIdResultSetExtractor.class)
        );

        if(ids == null){
            return new int[0];
        }

        final int lim = ids.size();
        int[] array = new int[lim];
        for(int i = 0; i < lim; i++) {
            array[i] = ids.get(i);
        }

        return array;
    }

    @Override
    @Transactional
    public int insertProductAndGetId(Product product, UserActivity userActivity, HashSet<Integer> categoriesIds) {

        // insert product
        // https://stackoverflow.com/questions/14537546/how-to-get-generated-id-after-i-inserted-into-a-new-data-record-in-database-usin
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement insertPreparedStatement = connection.prepareStatement(DEF_INSERT_PRODUCT_SQL, Statement.RETURN_GENERATED_KEYS);
            insertPreparedStatement.setFloat(1, product.getPrice());
            insertPreparedStatement.setInt(2, product.getStock());
            insertPreparedStatement.setString(3, product.getName());
            insertPreparedStatement.setString(4, product.getDescription());
            insertPreparedStatement.setString(5, product.getImageBase64());
            insertPreparedStatement.setTimestamp(6, product.getAdded());
            return insertPreparedStatement;
        }, keyHolder);

        Assert.notNull(keyHolder.getKey(), "Why is keyHolder.getKey() null ?");
        final int productId = keyHolder.getKey().intValue();

        // insert many-to-many relation
        jdbcTemplate.batchUpdate(DEF_INSERT_PRODUCT_CATEGORY_RELATION_MANY_TO_MANY_SQL, getCategoriesArgs(categoriesIds, productId));

        // mark activity
        jdbcTemplate.update(DEF_INSERT_PRODUCT_RELATED_ACTIVITY_SQL, getUserActivityArgs(userActivity));

        return productId;
    }

    @Override
    @Transactional
    public void updateProduct(Product product, UserActivity userActivity, HashSet<Integer> categoriesIds) {

        // update product
        Object[] args = {product.getPrice(), product.getStock(), product.getName(), product.getDescription(),
                product.getImageBase64(), product.getAdded(), product.getId()};
        jdbcTemplate.update(DEF_UPDATE_PRODUCT_SQL, args);

        // update many-to-many relation
        // unlink from previous categories
        jdbcTemplate.update(DEF_DELETE_PRODUCT_CATEGORY_RELATION_MANY_TO_MANY_SQL, product.getId());
        // link with new categories
        jdbcTemplate.batchUpdate(DEF_INSERT_PRODUCT_CATEGORY_RELATION_MANY_TO_MANY_SQL, getCategoriesArgs(categoriesIds, product.getId()));

        // mark activity
        jdbcTemplate.update(DEF_INSERT_PRODUCT_RELATED_ACTIVITY_SQL, getUserActivityArgs(userActivity));
    }

    @Override
    @Transactional
    public void deleteProduct(int productId, UserActivity userActivity) {
        // delete product
        jdbcTemplate.update(DEF_DELETE_PRODUCT_SQL, productId);

        // many-to-many relation wil be deleted automatically because of the 'DELETE ON CASCADE' set up on the DB

        // mark activity
        jdbcTemplate.update(DEF_INSERT_PRODUCT_RELATED_ACTIVITY_SQL, getUserActivityArgs(userActivity));
    }

    @Override
    public List<Product> filterProductsAndSortByDateAscending(int categoryId, String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_DATE_ASCENDING_SQL,
                getFilterArgs(categoryId, searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByDateAscending(String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_DATE_ASCENDING_SQL,
                getFilterArgs(searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByDateDescending(int categoryId, String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_DATE_DESCENDING_SQL,
                getFilterArgs(categoryId, searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByDateDescending(String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_DATE_DESCENDING_SQL,
                getFilterArgs(searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByPriceAscending(int categoryId, String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_PRICE_ASCENDING_SQL,
                getFilterArgs(categoryId, searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByPriceAscending(String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_PRICE_ASCENDING_SQL,
                getFilterArgs(searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByPriceDescending(int categoryId, String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_CATEGORY_AND_NAME_PATTERN_AND_SORT_BY_PRICE_DESCENDING_SQL,
                getFilterArgs(categoryId, searchValue, maxPrice)
        );
    }

    @Override
    public List<Product> filterProductsAndSortByPriceDescending(String searchValue, float maxPrice) {
        return filterAndSort(
                DEF_FILTER_BY_NAME_PATTERN_AND_SORT_BY_PRICE_DESCENDING_SQL,
                getFilterArgs(searchValue, maxPrice)
        );
    }


    private List<Product> filterAndSort(String sql, Object[] args){
        return jdbcTemplate.query(sql, args,  webApplicationContext.getBean(ProductsResultSetExtractor.class));
    }

    private Object[] getFilterArgs(int categoryId, String searchValue, float maxPrice){
        return new Object[]{categoryId, "%" + searchValue + "%", maxPrice};
    }

    private Object[] getFilterArgs(String searchValue, float maxPrice){
        return new Object[]{"%" + searchValue + "%", maxPrice};
    }

    private Object[] getUserActivityArgs(UserActivity userActivity){
        return new Object[]{userActivity.getTag(), userActivity.getBefore(), userActivity.getAfter(),
                userActivity.getAdded(), userActivity.getUserAccount().getEmail()};
    }

    private List<Object[]> getCategoriesArgs(HashSet<Integer> categoriesIds, int productId){
        List<Object[]> args = new ArrayList<>();
        categoriesIds.forEach(id -> args.add(new Object[]{productId, id}));
        return args;
    }
}
