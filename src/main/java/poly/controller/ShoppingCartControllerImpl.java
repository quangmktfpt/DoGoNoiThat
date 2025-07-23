package poly.controller;

import poly.dao.ShoppingCartDAO;
import poly.dao.impl.ShoppingCartDAOImpl;
import poly.entity.ShoppingCart;
import poly.entity.CartItem;
import java.util.List;

public class ShoppingCartControllerImpl implements ShoppingCartController {
    private ShoppingCartDAO dao = new ShoppingCartDAOImpl();

    @Override
    public ShoppingCart getCartByUserId(int userId) {
        return dao.selectByUserId(userId);
    }

    @Override
    public List<CartItem> getCartItems(int cartId) {
        return dao.getCartItems(cartId);
    }

    @Override
    public void addCartItem(int cartId, CartItem item) {
        dao.addCartItem(cartId, item);
    }

    @Override
    public void updateCartItem(int cartItemId, int quantity) {
        dao.updateCartItem(cartItemId, quantity);
    }

    @Override
    public void deleteCartItem(int cartItemId) {
        dao.deleteCartItem(cartItemId);
    }

    @Override
    public void clearCart(int cartId) {
        dao.clearCart(cartId);
    }
} 