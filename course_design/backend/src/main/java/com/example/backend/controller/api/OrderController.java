package com.example.backend.controller.api;

import com.alibaba.fastjson.JSON;
import com.example.backend.Service.GoodsService;
import com.example.backend.Service.OrderService;
import com.example.backend.entity.Goods;
import com.example.backend.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("api/insertOrder")
    public void insertOrder(String order){
        List<Order> orders = JSON.parseArray(order,Order.class);
        for(Order o:orders){
            orderService.insertOrder(o);
            goodsService.addSellnums(o.getGoodsid());
        }

    }

    @RequestMapping("api/getOrder/{name}")
    public String getOrder(@PathVariable("name") String username){
        System.out.println("name = "+username);
        List<Order> orders = orderService.selectAllOrder(username);
        List<Goods> goods = new ArrayList<>();
        for(Order o:orders){
            Goods g = new Goods();
            g.setGoodsid(o.getGoodsid());
            g.setImage1(o.getGoodsimage());
            g.setGoodsname(o.getGoodsname());
            g.setPrice(o.getGoodsprice());
            goods.add(g);
        }
        String str = JSON.toJSONString(goods);
        System.out.println(str);
        return str;
    }

}
