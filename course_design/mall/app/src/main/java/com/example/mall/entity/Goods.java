package com.example.mall.entity;


import java.io.Serializable;

/**
 * @Author: 李东雷
 * @TIME：17:14-2019/6/5
 */
public class Goods implements Serializable {

    private Long goodsid; //商品id
    private String storename; //商品所属店铺名称
    private String type; //商品类型   全新/二手
    private String catalog;  //商品种类
    private String secondcatalog;
    private String brand;   //商品品牌
    private String goodsname;    //商品名称
    private String username;    //店家用户名
    private String markettime;  //上市时间
    private String pubdate;     //商品在线发布时间
    private String purchaseplace;   //商品原产地     国内/国外及港澳台
    private Integer price;   //商品价格
    private Integer promotionprice;
    private Integer nums;   //商品数量
    private String image1;  //主图
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String description;    //商品描述信息
    private String address;     //发货地址
    private String express;     //物流公司
    private Integer sellnums=0;    //销量
    private Integer views=0;
    private Integer valid=0;
    private Integer onsell=1;//审核后置1

    public Goods() {
    }


    @Override
    public String toString() {
        return "Goods{" +
                "goodsid=" + goodsid +
                ", storename='" + storename + '\'' +
                ", type='" + type + '\'' +
                ", catalog='" + catalog + '\'' +
                ", secondcatalog='" + secondcatalog + '\'' +
                ", brand='" + brand + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", username='" + username + '\'' +
                ", markettime='" + markettime + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", purchaseplace='" + purchaseplace + '\'' +
                ", price=" + price +
                ", promotionprice=" + promotionprice +
                ", nums=" + nums +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", image4='" + image4 + '\'' +
                ", image5='" + image5 + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", express='" + express + '\'' +
                ", sellnums=" + sellnums +
                ", views=" + views +
                ", valid=" + valid +
                ", onsell=" + onsell +
                '}';
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getOnsell() {
        return onsell;
    }

    public void setOnsell(Integer onsell) {
        this.onsell = onsell;
    }


    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Long getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Long goodsid) {
        this.goodsid = goodsid;
    }

    public String getStroename() {
        return storename;
    }

    public void setStroename(String stroename) {
        this.storename = stroename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSecondcatalog() {
        return secondcatalog;
    }

    public void setSecondcatalog(String secondcatalog) {
        this.secondcatalog = secondcatalog;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMarkettime() {
        return markettime;
    }

    public void setMarkettime(String markettime) {
        this.markettime = markettime;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getPurchaseplace() {
        return purchaseplace;
    }

    public void setPurchaseplace(String purchaseplace) {
        this.purchaseplace = purchaseplace;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public Integer getPromotionprice() {
        return promotionprice;
    }

    public void setPromotionprice(Integer promotionprice) {
        this.promotionprice = promotionprice;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public Integer getSellnums() {
        return sellnums;
    }

    public void setSellnums(Integer sellnums) {
        this.sellnums = sellnums;
    }
}
