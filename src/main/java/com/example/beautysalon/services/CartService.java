package com.example.beautysalon.services;

import com.example.beautysalon.models.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private List<CartItem> cartItems;

    public CartService() {
        cartItems = new ArrayList<>();
    }

    public void addToCart(Object item, String type, String name, double price, String category) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getName().equals(name) && cartItem.getType().equals(type)) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(item, type, name, price, 1, category));
    }

    public void removeFromCart(CartItem item) {
        cartItems.remove(item);
    }

    public void updateQuantity(CartItem item, int quantity) {
        if (quantity <= 0) {
            cartItems.remove(item);
        } else {
            item.setQuantity(quantity);
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotal();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public int getItemCount() {
        return cartItems.size();
    }
}