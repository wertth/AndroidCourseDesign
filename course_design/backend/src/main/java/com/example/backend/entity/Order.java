package com.example.backend.entity;


import java.io.Serializable;

public class Order implements Serializable {

    private String username;
    private Long goodsid;
    private String goodsimage;
    private String goodsname;
    private Integer goodsprice;

    public Order() {
    }

    public Order(String username, Long goodsid, String goodsimage, String goodsname, Integer goodsprice) {
        this.username = username;
        this.goodsid = goodsid;
        this.goodsimage = goodsimage;
        this.goodsname = goodsname;
        this.goodsprice = goodsprice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Long goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsimage() {
        return goodsimage;
    }

    public void setGoodsimage(String goodsimage) {
        this.goodsimage = goodsimage;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public Integer getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(Integer goodsprice) {
        this.goodsprice = goodsprice;
    }
}
