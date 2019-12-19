package com.example.backend.Service;

import com.example.backend.entity.Shoucang;
import com.example.backend.mapper.ShoucangMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoucangService implements ShoucangMapper {

    @Autowired
    ShoucangMapper shoucangMapper;
    @Override
    public void insertShoucang(Shoucang shoucang) {
        shoucangMapper.insertShoucang(shoucang);
    }

    @Override
    public List<Shoucang> getShoucang(String username) {
        return shoucangMapper.getShoucang(username);
    }

    @Override
    public void deleteCollect(Long id) {
        shoucangMapper.deleteCollect(id);
    }
}
