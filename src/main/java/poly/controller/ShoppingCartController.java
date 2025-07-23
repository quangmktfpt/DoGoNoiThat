package poly.controller;

import poly.entity.ShoppingCart;
import poly.entity.CartItem;
import java.util.List;

public interface ShoppingCartController {
    ShoppingCart getCartByUserId(int userId);
    List<CartItem> getCartItems(int cartId);
    void addCartItem(int cartId, CartItem item);
    void updateCartItem(int cartItemId, int quantity);
    void deleteCartItem(int cartItemId);
    void clearCart(int cartId);
} 