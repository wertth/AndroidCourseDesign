package com.example.backend.controller.api;

import com.alibaba.fastjson.JSON;
import com.example.backend.Service.GoodsService;
import com.example.backend.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class getGoods {

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/getAll")
    public List<Goods> getAll() {
        return goodsService.selectHotGoods();
    }
    @GetMapping("/getGoodsById/{id}")
    public Goods getGoodsById(@PathVariable("id") Long id) {
        System.out.println("hello goodsid");
        return goodsService.selectGoodsById(id);
    }
    @RequestMapping("/getGoodsByName/{goodsname}")
    public List<Goods> getGoodsByName(@PathVariable("goodsname") String goodsname){
        return goodsService.selectGoodsByName("%"+goodsname+"%");
    }
    @RequestMapping("api/getGoodsOrder")
    public String getGoodsOrder(String list){
        System.out.println("getOrderList");
        List<Goods> goods = new ArrayList<>();
        System.out.println(list);
        List<Long> s = JSON.parseArray(list,Long.class);
        for(Long i:s){
            Goods g = goodsService.selectGoodsById(i);
            goods.add(g);
        }
        System.out.println(JSON.toJSONString(goods));
        return JSON.toJSONString(goods);
    }
}
