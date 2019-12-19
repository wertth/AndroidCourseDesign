package com.example.backend.controller.api;

import com.alibaba.fastjson.JSON;
import com.example.backend.Service.ShoucangService;
import com.example.backend.entity.Goods;
import com.example.backend.entity.Shoucang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ShoucangController {

    @Autowired
    ShoucangService shoucangService;

    @RequestMapping("/addCollect")
    public void insertCollect(String shoucang){
        System.out.println("收藏 = "+shoucang);
        Shoucang shoucang1 = JSON.parseObject(shoucang,Shoucang.class);
        shoucangService.insertShoucang(shoucang1);
    }
    @RequestMapping("/deleteCollect/{id}")
    public void deleteCollect(@PathVariable("id")Long goodsid){
        System.out.println("收藏 = "+goodsid);
        //Shoucang shoucang1 = JSON.parseObject(shoucang,Shoucang.class);
        shoucangService.deleteCollect(goodsid);
    }

    @RequestMapping("/getCollect/{name}")
    public String getOrder(@PathVariable("name") String username){
        System.out.println("name = "+username);
        List<Shoucang> shoucang = shoucangService.getShoucang(username);
        List<Goods> goods = new ArrayList<>();
        for(Shoucang o:shoucang){
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
