package com.example.backend.Service;

import com.example.backend.entity.Cart;
import com.example.backend.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService implements CartMapper{

    @Autowired
    CartMapper cartMapper;

    @Override
    public void insertCart(Cart cart) {
        cartMapper.insertCart(cart);
    }

    @Override
    public Integer getNums(String username) {
        return cartMapper.getNums(username);
    }

    @Override
    public void deleteCart(Long goodsid) {
        cartMapper.deleteCart(goodsid);
    }

    @Override
    public List<Cart> selectCart() {
        return cartMapper.selectCart();
    }
}
