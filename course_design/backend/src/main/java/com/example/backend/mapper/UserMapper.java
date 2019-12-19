package com.example.backend.mapper;

import com.example.backend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @Author: 李东雷
 * @TIME：9:54-2019/6/5
 */
@Mapper
@Repository
public interface UserMapper {

    @Insert("insert into user(username, password, logo, gender, tel,addr,address,postcode,headpic)" +
            " values(#{username},#{password},#{logo},#{gender},#{tel}, #{addr},#{address}, #{postcode},#{headpic})")
    void registerUser(User user);

    @Select("select password from user where username = #{username}")
    String findPasswordByUsername(String username);

    @Select("select headpic from user where username = #{username}")
    String getHeadpic(String username);

    @Select("select role from user where username = #{username}")
    String findRoleByUsername(String username);

    @Select("select * from user where username = #{username}")
    User selectUser(String username);

    @Update("update user set username = #{username},logo=#{logo},email=#{email},tel=#{tel},gender=#{gender},birthday=#{birthday},lasttime=#{lasttime} where regtime=#{regtime}")
    void updateUser(User user);

    //判断邮箱是否已经注册
    @Select("select count(*) from user where email = #{email}")
    Integer isHaveEmail(String email);

    //更新权限
    @Update("update user set role = 'shopkeeper' where username=#{username}")
    void updateRole(String username);

}

