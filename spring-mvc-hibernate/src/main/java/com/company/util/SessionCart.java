package com.company.util;

import com.company.dto.CartProductDto;
import com.company.model.Product;
import com.company.service.ProductService;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.IntStream;

@Getter
@ToString
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SessionCart {

    public static final int MAX_ITEMS_PER_TYPE = 5;

    private final HashSet<CartProductDto> cart = new LinkedHashSet<>();
    private final int[] idxList = IntStream.rangeClosed(1, MAX_ITEMS_PER_TYPE).toArray();
    private double totalAmount;
    private boolean isCartValid;

    private final ProductService productService;

    @Autowired
    public SessionCart(ProductService productService) {
        this.productService = productService;
    }

    public void addProduct(int productId){
        for(CartProductDto cartProductDto : cart){
            if(cartProductDto.getProductId() == productId){
                return;
            }
        }

        Product product = productService.getProductById(productId);
        if(product.getStock() > 0){
            cart.add(getConversion(product, 1));
        }
    }

    public void updateProductQuantity(int productId, int newQuantity){
        for(CartProductDto cartProductDto : cart){
            if(cartProductDto.getProductId() == productId){
                cartProductDto.setQuantity(newQuantity);
                cartProductDto.setTotalPrice(newQuantity * cartProductDto.getProductPrice());
                break;
            }
        }
        calculateCartAmount();
    }

    public void removeProduct(int productId){
        cart.removeIf(tmp -> tmp.getProductId() == productId);
        calculateCartAmount();
    }

    public void refreshCart(){
        refreshCart(false);
    }

    private void refreshCart(boolean checkCartValidity){
        if(checkCartValidity){
            isCartValid = true;
        }

        LinkedHashSet<CartProductDto> newCart = new LinkedHashSet<>();
        cart.forEach(cartProduct -> {
            CartProductDto tmp = getConversion(productService.getProductById(cartProduct.getProductId()), cartProduct.getQuantity());
            newCart.add(tmp);

            if(checkCartValidity && isCartValid){
                if(tmp.isOutOfStock() || tmp.isTooMuchQuantity()){
                    isCartValid = false;
                }
            }
        });

        cart.clear();
        cart.addAll(newCart);
        calculateCartAmount();
    }

    private void calculateCartAmount(){
        totalAmount = cart.stream().mapToDouble(CartProductDto::getTotalPrice).sum();
    }

    public int cartSize(){
        return cart.size();
    }

    public double getTotalAmount() {
        return GeneralUtilities.getDoubleFormatted(totalAmount, "######.##");
    }

    private CartProductDto getConversion(Product product, int quantity){
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setProductId(product.getId());
        cartProductDto.setProductName(product.getName());
        cartProductDto.setQuantity(quantity);
        cartProductDto.setProductPrice(product.getPrice());
        cartProductDto.setTotalPrice(quantity * product.getPrice());
        cartProductDto.setAvailabilityStatus(product.getStock());
        return cartProductDto;
    }

    public boolean checkIfProductIsAdded(int productId){
        for(CartProductDto cartProductDto : cart){
            if(cartProductDto.getProductId() == productId){
                return true;
            }
        }
        return false;
    }

    public boolean isCartValid(){
        refreshCart(true);
        return isCartValid;
    }

    public void clearCart(){
        cart.clear();
        totalAmount = 0;
        isCartValid = false;
    }

}
