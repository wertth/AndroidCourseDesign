package com.example.mall.entity;

import java.io.Serializable;

public class Cart implements Serializable {
    public Long id;
    public Long goodsid;
    public String goodsname;
    public String image1;
    public Integer price;

    public Cart() {
    }

    public Cart(Long id, Long goodsid, String goodsname, String image1, Integer price) {
        this.id = id;
        this.goodsid = goodsid;
        this.goodsname = goodsname;
        this.image1 = image1;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", goodsid=" + goodsid +
                ", goodsname='" + goodsname + '\'' +
                ", image1='" + image1 + '\'' +
                ", price=" + price +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Long goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
