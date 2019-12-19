package com.example.backend.mapper;

import com.example.backend.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {

    @Insert("insert into order_mall values(#{username},#{goodsid},#{goodsimage},#{goodsname},#{goodsprice})")
    public void insertOrder(Order order);

    @Select("select * from order_mall where username = #{username}")
    public List<Order> selectAllOrder(String username);
}
