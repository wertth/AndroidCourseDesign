package com.example.backend.mapper;

import com.example.backend.entity.Shoucang;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShoucangMapper {

    @Insert("insert into shoucang values(#{username},#{goodsid},#{goodsimage},#{goodsname},#{goodsprice})")
    void insertShoucang(Shoucang shoucang);

    @Select("select * from shoucang where username = #{username}")
    List<Shoucang> getShoucang(String username);

    @Delete("delete from shoucang where goodsid = #{id}")
    void deleteCollect(Long id);
}
