package com.example.backend.controller.api;

import com.alibaba.fastjson.JSON;
import com.example.backend.Service.CartService;
import com.example.backend.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class cartController {

    @Autowired
    CartService cartService;

    @RequestMapping("api/getCartAll")
    public List<Cart> getCartAll(){
        System.out.println("cart............");
        return cartService.selectCart();
    }
    @RequestMapping("api/addCart")
    public void addCart(String cart){
        Cart cart1 = JSON.parseObject(cart,Cart.class);
        System.out.println("cart = "+cart);
        System.out.println("cart............");
        cartService.insertCart(cart1);
    }
    @RequestMapping("api/deleteCart")
    public void deleteCart(String cart){

        System.out.println(cart);
        List<Long> s = JSON.parseArray(cart,Long.class);
        for(Long i:s){
            System.out.println(i);
            cartService.deleteCart(i);
        }
    }

}
