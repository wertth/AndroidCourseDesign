package com.example.backend;

import com.alibaba.fastjson.JSON;
import com.example.backend.Service.GoodsService;
import com.example.backend.entity.Goods;
import com.example.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BackendApplicationTests {



    @Autowired
    GoodsService goodsService;
    @Test
    void contextLoads() {
        List<Goods> goods = goodsService.selectGoodsByName("%苹果%");
        System.out.println(goods);
    }

}
