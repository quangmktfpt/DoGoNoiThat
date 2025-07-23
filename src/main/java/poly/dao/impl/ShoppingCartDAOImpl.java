package poly.dao.impl;

import poly.dao.ShoppingCartDAO;
import poly.entity.ShoppingCart;
import poly.entity.CartItem;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDAOImpl implements ShoppingCartDAO {
    private final String INSERT_SQL = "INSERT INTO ShoppingCarts (UserID) VALUES (?)";
    private final String UPDATE_SQL = "UPDATE ShoppingCarts SET UserID=? WHERE CartID=?";
    private final String DELETE_SQL = "DELETE FROM ShoppingCarts WHERE CartID=?";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM ShoppingCarts WHERE CartID=?";
    private final String SELECT_BY_USER_SQL = "SELECT * FROM ShoppingCarts WHERE UserID=?";
    private final String SELECT_CART_ITEMS_SQL = "SELECT * FROM CartItems WHERE CartID=?";
    private final String INSERT_CART_ITEM_SQL = "INSERT INTO CartItems (CartID, ProductID, Quantity) VALUES (?, ?, ?)";
    private final String UPDATE_CART_ITEM_SQL = "UPDATE CartItems SET Quantity=? WHERE CartItemID=?";
    private final String DELETE_CART_ITEM_SQL = "DELETE FROM CartItems WHERE CartItemID=?";
    private final String CLEAR_CART_SQL = "DELETE FROM CartItems WHERE CartID=?";

    @Override
    public void insert(ShoppingCart cart) {
        XJdbc.executeUpdate(INSERT_SQL, cart.getUserId());
    }

    @Override
    public void update(ShoppingCart cart) {
        XJdbc.executeUpdate(UPDATE_SQL, cart.getUserId(), cart.getCartId());
    }

    @Override
    public void delete(int cartId) {
        XJdbc.executeUpdate(DELETE_SQL, cartId);
    }

    @Override
    public ShoppingCart selectById(int cartId) {
        List<ShoppingCart> list = selectBySql(SELECT_BY_ID_SQL, cartId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public ShoppingCart selectByUserId(int userId) {
        List<ShoppingCart> list = selectBySql(SELECT_BY_USER_SQL, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<CartItem> getCartItems(int cartId) {
        List<CartItem> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(SELECT_CART_ITEMS_SQL, cartId);
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("CartItemID"));
                item.setCartId(rs.getInt("CartID"));
                item.setProductId(rs.getString("ProductID"));
                item.setQuantity(rs.getInt("Quantity"));
                list.add(item);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void addCartItem(int cartId, CartItem item) {
        XJdbc.executeUpdate(INSERT_CART_ITEM_SQL, cartId, item.getProductId(), item.getQuantity());
    }

    @Override
    public void updateCartItem(int cartItemId, int quantity) {
        XJdbc.executeUpdate(UPDATE_CART_ITEM_SQL, quantity, cartItemId);
    }

    @Override
    public void deleteCartItem(int cartItemId) {
        XJdbc.executeUpdate(DELETE_CART_ITEM_SQL, cartItemId);
    }

    @Override
    public void clearCart(int cartId) {
        XJdbc.executeUpdate(CLEAR_CART_SQL, cartId);
    }

    private List<ShoppingCart> selectBySql(String sql, Object... args) {
        List<ShoppingCart> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                ShoppingCart cart = new ShoppingCart();
                cart.setCartId(rs.getInt("CartID"));
                cart.setUserId(rs.getInt("UserID"));
                cart.setCreatedDate(rs.getTimestamp("CreatedDate") != null ? rs.getTimestamp("CreatedDate").toLocalDateTime() : null);
                list.add(cart);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
} 