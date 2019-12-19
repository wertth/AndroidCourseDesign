package com.example.backend.mapper;

import com.example.backend.entity.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CartMapper {

    //插入购物车
    // ( id, username, storename,goodsid,nums, price ,time ,image)
    @Insert("insert into cart values(#{id},#{goodsid},#{goodsname},#{image1},#{price})")
    void insertCart(Cart cart);

    //购物车数量
    @Select("select count(*) from cart where username=#{username}")
    Integer getNums(String username);

    //根据用户名查询购物车
    @Select("select * from cart")
    List<Cart> selectCart();

    //根据用户名查询购物车
    @Delete("delete from cart where goodsid = #{goodsid}")
    void deleteCart(Long goodsid);
}
