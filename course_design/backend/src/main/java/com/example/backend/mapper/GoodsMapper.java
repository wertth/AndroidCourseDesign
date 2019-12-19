package com.example.backend.mapper;

import com.example.backend.entity.Goods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 李东雷
 * @TIME：9:06-2019/6/6
 */
@Mapper
@Repository
public interface GoodsMapper {
    //goodsid
//storename
//type
//catalog
//brand
//goodsname
//username
//markettime
//pubdate
//purchaseplace
//price
//nums
//image1
//image2
//image3
//image4
//image5
//description
//address
//express
//sellnums
    //views
    @Insert("insert into goods values(#{goodsid},#{storename},#{type},#{catalog},#{secondcatalog},#{brand},#{goodsname}" +
            ",#{username},#{markettime},#{pubdate},#{purchaseplace},#{price},#{promotionprice},#{nums},#{image1},#{image2},#{image3}" +
            ",#{image4},#{image5},#{description},#{address},#{express},#{sellnums},#{views},#{valid},#{onsell})")
    void insertGoods(Goods goods);

    // 首页 选取热门商品
    @Select("select * from goods order by sellnums desc limit 30")
    List<Goods> selectHotGoods();

    @Select("select * from goods where goodsid = #{goodsid}")
    Goods selectGoodsById(Long goodsid);

    // 查找商品根据商品名 模糊查询
    @Select("select * from goods where goodsname like #{goodsname} or secondcatalog like #{goodsname}")
    List<Goods> selectGoodsByName(String goodsname);

    @Select("select * from goods where username = #{username}")
    List<Goods> selectGoodsByUsername(String username);

    @Select("select storename from goods where username = #{username}")
    String selectStorenameByUsername(String username);

    //待补货
    @Select("select count(*) from goods where nums<1")
    Integer waitAdd();
    //下架商品数量
    @Select("select count(*) from goods where onsell = 0")
    Integer notOnsell();
    // 下架商品
    @Update("update goods set onsell=0 where goodsid=#{goodsid}")
    void UndercarriageGoods(Long goodsid);

    //更新商品
    @Update("update goods set storename=#{storename},type=#{type},catalog=#{catalog},secondcatalog=#{secondcatalog}," +
            "brand=#{brand},goodsname=#{goodsname},username=#{username},markettime=#{markettime},pubdate=#{pubdate}," +
            "purchaseplace=#{purchaseplace},price=#{price},promotionprice=#{promotionprice},nums=#{nums}," +
            "image1=#{image1},image2=#{image2},image3=#{image3},image4=#{image4},image5=#{image5},description=#{description}," +
            "address=#{address},express=#{express},sellnums=#{sellnums},views=#{views},valid=#{valid},onsell=#{onsell}" +
            " where goodsid=#{goodsid}")
    void updateGoods(Goods goods);


    // 查找待审核商品
    @Select("select * from goods where valid=0")
    List<Goods> selectWaitVerify();

    // 审核通过商品

    @Update("update goods set valid=1 where goodsid=#{goodsid}")
    void goodsVerifyPass(Long goodsid);

    //添加浏览量
    @Update("update goods set views = views+1 where goodsid = #{goodsid}")
    void addViews(Long goodsid);

    //添加销量
    @Update("update goods set sellnums = sellnums+1 where goodsid = #{goodsid}")
    void addSellnums(Long goodsid);


}

