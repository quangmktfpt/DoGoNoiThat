/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.entity;

import java.time.LocalDateTime;

/**
 *
 * @author quang
 */
public class ShoppingCart {
     private Integer cartId;
    private Integer userId;
    private LocalDateTime createdDate;

    public ShoppingCart(Integer cartId, Integer userId, LocalDateTime createdDate) {
        this.cartId = cartId;
        this.userId = userId;
        this.createdDate = createdDate;
    }

    public ShoppingCart() {
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
}
