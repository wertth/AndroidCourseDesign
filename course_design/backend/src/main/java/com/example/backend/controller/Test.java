package com.example.backend.controller;

import com.example.backend.Service.GoodsService;
import com.example.backend.Service.UserService;
import com.example.backend.entity.Goods;
import com.example.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Test {

    @Autowired
    UserService userService;
    @Autowired
    public GoodsService goodsService;
    @Autowired
    UserMapper userMapper;

    @RequestMapping("/test")
    public String getHello() {
        //new BCryptPasswordEncoder().encode(user.getPassword())
        //String pass = "ldl990330.";
        //String md5Password = DigestUtils.md5DigestAsHex(pass.getBytes());
        String md5Password = userMapper.findPasswordByUsername("lidonglei");
        return md5Password;
    }
    @RequestMapping("/index")
    public ModelAndView idnex(){
        return new ModelAndView("index");
    }
    @RequestMapping("/get")
    public Goods getGoods2(){
        return goodsService.selectGoodsById(1576115720756L);
    }

}
